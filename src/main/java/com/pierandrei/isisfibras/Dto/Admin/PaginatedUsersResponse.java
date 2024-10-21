package com.pierandrei.isisfibras.Dto.Admin;

import java.util.List;

public record PaginatedUsersResponse(
        List<UsersByRoleResponse> users,
        int totalPages,
        long totalElements
) {
}
