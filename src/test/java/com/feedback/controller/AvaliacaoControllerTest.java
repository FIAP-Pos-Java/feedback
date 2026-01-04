package com.feedback.controller;

import com.feedback.dto.AvaliacaoRequest;
import com.feedback.dto.AvaliacaoResponse;
import com.feedback.service.AvaliacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvaliacaoController.class)
class AvaliacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AvaliacaoService avaliacaoService;

    @Test
    void deveCriarAvaliacaoComSucesso() throws Exception {
        // Arrange
        AvaliacaoRequest request = new AvaliacaoRequest("Ótimo serviço", 9);
        AvaliacaoResponse response = new AvaliacaoResponse("123", "Avaliação registrada com sucesso");
        
        when(avaliacaoService.processarAvaliacao(any(AvaliacaoRequest.class)))
            .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/avaliacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.mensagem").value("Avaliação registrada com sucesso"));
    }

    @Test
    void deveRetornarBadRequestQuandoNotaInvalida() throws Exception {
        // Arrange
        AvaliacaoRequest request = new AvaliacaoRequest("Teste", 15);

        // Act & Assert
        mockMvc.perform(post("/avaliacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoDescricaoVazia() throws Exception {
        // Arrange
        AvaliacaoRequest request = new AvaliacaoRequest("", 5);

        // Act & Assert
        mockMvc.perform(post("/avaliacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

