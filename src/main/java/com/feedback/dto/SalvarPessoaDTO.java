package com.feedback.dto;

import software.amazon.awssdk.annotations.NotNull;

public record SalvarPessoaDTO(
        @NotNull
        String nome,

        @NotNull
        String endereco,

        @NotNull
        String email
) {
}
