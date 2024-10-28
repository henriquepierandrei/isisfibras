package com.pierandrei.isisfibras.Dto.Admin;

public record CouponCreateResponse(
        String code,
        String description,
        boolean freeShipping,
        String message
) {
}
