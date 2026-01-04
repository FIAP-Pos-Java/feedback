package com.feedback.controller;

import com.feedback.dto.AvaliacaoRequest;
import com.feedback.dto.AvaliacaoResponse;
import com.feedback.dto.ErrorResponse;
import com.feedback.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoController {

    private static final Logger LOG = LoggerFactory.getLogger(AvaliacaoController.class);

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<?> criarAvaliacao(@Valid @RequestBody AvaliacaoRequest request) {
        try {
            var response = avaliacaoService.processarAvaliacao(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } 
        catch (IllegalArgumentException e) 
        {
            LOG.error("Erro de validacao", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Erro de validação: " + e.getMessage()));
        } 
        catch (Exception e) 
        {
            LOG.error("Erro ao processar avaliacao", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno ao processar avaliação"));
        }
    }
}

