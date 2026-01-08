package com.feedback.dto;

import software.amazon.awssdk.annotations.NotNull;

import java.util.UUID;

public record AtualizarPessoaDTO(
        @NotNull
        UUID id,

        @NotNull
        String nome,

        @NotNull
        String endereco,

        @NotNull
        String email
) {
}
