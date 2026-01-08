package com.feedback.dto;

import software.amazon.awssdk.annotations.NotNull;

public record PessoaDTO(
        @NotNull
        String nome,

        @NotNull
        String endereco,

        @NotNull
        String email
) {
}
