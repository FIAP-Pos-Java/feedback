package com.feedback.dto;

public class AvaliacaoResponse {

    private String id;
    private String mensagem;

    public AvaliacaoResponse() {
    }

    public AvaliacaoResponse(String id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

