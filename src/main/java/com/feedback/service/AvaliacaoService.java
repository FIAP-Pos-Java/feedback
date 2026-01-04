package com.feedback.service;

import com.feedback.dto.AvaliacaoRequest;
import com.feedback.dto.AvaliacaoResponse;
import com.feedback.model.Feedback;
import com.feedback.repository.FeedbackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class AvaliacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(AvaliacaoService.class);

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SnsClient snsClient;

    @Value("${aws.sns.topic.arn:}")
    private String snsTopicArn;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public AvaliacaoResponse processarAvaliacao(AvaliacaoRequest request) {
        validarAvaliacao(request);
        Feedback feedback = Feedback.fromRequest(request.getDescricao(), request.getNota());
        Feedback feedbackSalvo = feedbackRepository.save(feedback);

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
            LOG.info("Alerta crítico publicado no SNS. Feedback ID: {}, Nota: {}", 
                feedback.getId(), feedback.getNota());
        } catch (SnsException e) {
            LOG.error("Erro ao publicar alerta crítico no SNS. Feedback ID: {}", feedback.getId(), e);
        } catch (Exception e) {
            LOG.error("Erro ao serializar feedback para SNS. Feedback ID: {}", feedback.getId(), e);
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
