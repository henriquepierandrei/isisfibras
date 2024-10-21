package com.pierandrei.isisfibras.Dto.Admin;

import com.pierandrei.isisfibras.Enuns.RolesUsers;

import java.util.UUID;

public record UsersByRoleResponse(
        UUID id,
        String email,
        String phone,
        RolesUsers role

) {
}
