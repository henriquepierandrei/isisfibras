package com.pierandrei.isisfibras.Dto.User;

public record ProductResponse(
        String sku,
        String name,
        String finalPrice,
        int quantity,
        String imageProductPrincipal
) {
}
