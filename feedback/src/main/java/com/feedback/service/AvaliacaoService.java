package com.feedback.service;

import com.feedback.dto.AvaliacaoRequest;
import com.feedback.dto.AvaliacaoResponse;
import com.feedback.model.Feedback;
import com.feedback.repository.FeedbackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

@ApplicationScoped
public class AvaliacaoService {

    private static final Logger LOG = Logger.getLogger(AvaliacaoService.class);

    @Inject
    FeedbackRepository feedbackRepository;

    @Inject
    SnsClient snsClient;

    @ConfigProperty(name = "dynamodb.table.name", defaultValue = "feedbacks")
    String tableName;

    @ConfigProperty(name = "sns.topic.arn", defaultValue = "")
    String snsTopicArn;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AvaliacaoResponse processarAvaliacao(AvaliacaoRequest request) {
        validarAvaliacao(request);
        Feedback feedback = Feedback.fromRequest(request.getDescricao(), request.getNota());
        feedbackRepository.initTableName(tableName);
        Feedback feedbackSalvo = feedbackRepository.salvar(feedback);

        // Se o feedback for crítico (nota <= 3), publica no SNS
        if (feedbackSalvo.getCritico() != null && feedbackSalvo.getCritico()) {
            publicarAlertaCritico(feedbackSalvo);
        }

        return new AvaliacaoResponse(
            feedbackSalvo.getId(),
            "Avaliação registrada com sucesso"
        );
    }

    private void publicarAlertaCritico(Feedback feedback) {
        if (snsTopicArn == null || snsTopicArn.isEmpty()) {
            LOG.warn("SNS Topic ARN não configurado. Alerta crítico não será publicado.");
            return;
        }

        try {
            String mensagem = objectMapper.writeValueAsString(feedback);
            
            PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(snsTopicArn)
                .subject("Alerta: Feedback Crítico Recebido")
                .message(mensagem)
                .build();

            snsClient.publish(publishRequest);
            LOG.infof("Alerta crítico publicado no SNS. Feedback ID: %s, Nota: %d", 
                feedback.getId(), feedback.getNota());
        } catch (SnsException e) {
            LOG.errorf(e, "Erro ao publicar alerta crítico no SNS. Feedback ID: %s", feedback.getId());
            // Não lança exceção para não quebrar o fluxo principal
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao serializar feedback para SNS. Feedback ID: %s", feedback.getId());
        }
    }

    private void validarAvaliacao(AvaliacaoRequest request) {
        if (request.getDescricao() == null || request.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode estar vazia");
        }
        if (request.getNota() == null || request.getNota() < 0 || request.getNota() > 10) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 10");
        }
    }
}
