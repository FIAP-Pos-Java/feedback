package com.feedback.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AvaliacaoRequest {

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota deve ser entre 0 e 10")
    @Max(value = 10, message = "A nota deve ser entre 0 e 10")
    private Integer nota;

    public AvaliacaoRequest() {
    }

    public AvaliacaoRequest(String descricao, Integer nota) {
        this.descricao = descricao;
        this.nota = nota;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}

