package com.pierandrei.isisfibras.Dto.Auth;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record RegisterDto(

        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String name,
        @NotBlank LocalDateTime dateBorn,
        @NotBlank String cpf
) {
}
