package com.feedback.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tb_administrador")
public final class Administrador extends Pessoa{

    public Administrador() {}

    public Administrador(UUID id, String nome, String endereco, String email) {
        super(id, nome, endereco, email);
    }
}
