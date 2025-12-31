package com.feedback.service;

import com.feedback.dto.AvaliacaoRequest;
import com.feedback.dto.AvaliacaoResponse;
import com.feedback.model.Feedback;
import com.feedback.repository.FeedbackRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AvaliacaoService {

    private static final Logger LOG = Logger.getLogger(AvaliacaoService.class);

    @Inject
    FeedbackRepository feedbackRepository;

    @ConfigProperty(name = "dynamodb.table.name", defaultValue = "feedbacks")
    String tableName;

    public AvaliacaoResponse processarAvaliacao(AvaliacaoRequest request) {
        validarAvaliacao(request);
        Feedback feedback = Feedback.fromRequest(request.getDescricao(), request.getNota());
        feedbackRepository.initTableName(tableName);
        Feedback feedbackSalvo = feedbackRepository.salvar(feedback);

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
}
