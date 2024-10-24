package com.pierandrei.isisfibras.Dto.Admin;

import java.util.List;

public record PaginatedUsersResponse(
        List<UserResponse> users,
        int totalPages,
        long totalElements
) {
}
