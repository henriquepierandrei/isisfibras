package com.pierandrei.isisfibras.Dto.Admin;

import com.pierandrei.isisfibras.Enuns.RolesUsers;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String phone,
        RolesUsers role

) {
}
