package com.pierandrei.isisfibras.Dto.Auth;

import java.util.UUID;

public record LoginResponse(String token, String name, UUID idUser, String message) {
}
