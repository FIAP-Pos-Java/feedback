package com.feedback.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Column(nullable = false)
    private Integer nota;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private Boolean critico;

    public Feedback() {
    }

    public Feedback(String id, String descricao, Integer nota, LocalDateTime dataCriacao, Boolean critico) {
        this.id = id;
        this.descricao = descricao;
        this.nota = nota;
        this.dataCriacao = dataCriacao;
        this.critico = critico;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getCritico() {
        return critico;
    }

    public void setCritico(Boolean critico) {
        this.critico = critico;
    }

    public static Feedback fromRequest(String descricao, Integer nota) {
        var id = UUID.randomUUID().toString();
        var dataCriacao = LocalDateTime.now();
        var critico = nota <= 3;

        return new Feedback(id, descricao, nota, dataCriacao, critico);
    }
}
