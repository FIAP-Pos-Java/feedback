package com.feedback.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tb_estudante")
public final class Estudante extends Pessoa{

    public Estudante() {}

    public Estudante(UUID id, String nome, String endereco, String email) {
        super(id, nome, endereco, email);
    }


}
