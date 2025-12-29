package com.feedback.service;

import com.feedback.dto.AvaliacaoRequest;
import com.feedback.dto.AvaliacaoResponse;
import com.feedback.model.Feedback;
import com.feedback.repository.FeedbackRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class AvaliacaoService {

    private static final Logger LOG = Logger.getLogger(AvaliacaoService.class);

    @Inject
    FeedbackRepository feedbackRepository;

    @Inject
    SnsClient snsClient;

    @ConfigProperty(name = "sns.topic.arn")
    String snsTopicArn;

    @ConfigProperty(name = "dynamodb.table.name", defaultValue = "feedbacks")
    String tableName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AvaliacaoResponse processarAvaliacao(AvaliacaoRequest request) {
        validarAvaliacao(request);
        Feedback feedback = Feedback.fromRequest(request.getDescricao(), request.getNota());
        feedbackRepository.initTableName(tableName);
        Feedback feedbackSalvo = feedbackRepository.salvar(feedback);

        if (feedbackSalvo.getCritico()) {
            publicarEventoCritico(feedbackSalvo);
        }

        return new AvaliacaoResponse(
            feedbackSalvo.getId(),
            "Avaliação registrada com sucesso"
        );
    }

    private void validarAvaliacao(AvaliacaoRequest request) {
        if (request.getDescricao() == null || request.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode estar vazia");
        }
        if (request.getNota() == null || request.getNota() < 0 || request.getNota() > 10) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 10");
        }
    }

    private void publicarEventoCritico(Feedback feedback) {
        if (snsTopicArn == null || snsTopicArn.isEmpty()) {
            LOG.warn("SNS Topic ARN não configurado. Evento crítico não será publicado.");
            return;
        }

        try {
            String mensagem = criarMensagemEvento(feedback);
            
            PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(snsTopicArn)
                .message(mensagem)
                .subject("Feedback Crítico Recebido")
                .build();

            snsClient.publish(publishRequest);
            LOG.infof("Evento crítico publicado no SNS. Feedback ID: %s", feedback.getId());
        } catch (SnsException e) {
            LOG.errorf(e, "Erro ao publicar evento crítico no SNS. Feedback ID: %s", feedback.getId());
        }
    }

    private String criarMensagemEvento(Feedback feedback) {
        try {
            return objectMapper.writeValueAsString(feedback);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao serializar feedback para JSON");
            return String.format(
                "{\"id\":\"%s\",\"descricao\":\"%s\",\"nota\":%d,\"dataCriacao\":\"%s\",\"critico\":true}",
                feedback.getId(),
                feedback.getDescricao().replace("\"", "\\\""),
                feedback.getNota(),
                feedback.getDataCriacao()
            );
        }
    }
}

