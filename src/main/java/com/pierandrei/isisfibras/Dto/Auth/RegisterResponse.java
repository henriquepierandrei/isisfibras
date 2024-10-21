package com.pierandrei.isisfibras.Dto.Auth;

import java.util.UUID;

public record RegisterResponse(
        String name,
        UUID idUser,
        String email,
        String message,
        String token
) {
}
