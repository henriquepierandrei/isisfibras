package com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto;

import java.time.LocalDateTime;

public record ProductCreateResponseDto(
        String sku,
        LocalDateTime createdAt,
        String message
) {
}
