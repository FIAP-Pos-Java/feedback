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

import java.util.Optional;

@ApplicationScoped
public class AvaliacaoService {

    private static final Logger LOG = Logger.getLogger(AvaliacaoService.class);

    @Inject
    FeedbackRepository feedbackRepository;

    @Inject
    SnsClient snsClient;

    @ConfigProperty(name = "sns.topic.arn")
    Optional<String> snsTopicArn;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AvaliacaoResponse processarAvaliacao(AvaliacaoRequest request) {
        validarAvaliacao(request);
        Feedback feedback = Feedback.fromRequest(request.getDescricao(), request.getNota());
        Feedback feedbackSalvo = feedbackRepository.salvar(feedback);

        if (feedbackSalvo.getCritico() != null && feedbackSalvo.getCritico()) {
            publicarAlertaCritico(feedbackSalvo);
        }

        return new AvaliacaoResponse(
            feedbackSalvo.getId(),
            "Avaliação registrada com sucesso"
        );
    }

    private void publicarAlertaCritico(Feedback feedback) {
        if (snsTopicArn.isEmpty() || snsTopicArn.get().isEmpty()) {
            LOG.warn("SNS Topic ARN não configurado. Alerta crítico não será publicado.");
            return;
        }

        try {
            String mensagem = objectMapper.writeValueAsString(feedback);
            
            PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(snsTopicArn.get())
                .subject("Alerta: Feedback Crítico Recebido")
                .message(mensagem)
                .build();

            snsClient.publish(publishRequest);
            LOG.infof("Alerta crítico publicado no SNS. Feedback ID: %s, Nota: %d", 
                feedback.getId(), feedback.getNota());
        } catch (SnsException e) {
            LOG.errorf(e, "Erro ao publicar alerta crítico no SNS. Feedback ID: %s", feedback.getId());
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
