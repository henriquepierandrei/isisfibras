package com.pierandrei.isisfibras.Dto.Admin;

import java.time.LocalDateTime;

public record CouponCreateDto(
        String couponCode,
        String name,
        double valuePerCentDiscount,
        boolean freeShipping,
        double minimumAmount,
        boolean couponActive,
        LocalDateTime expirationDate,
        int usageLimit,
        boolean singleUse,
        LocalDateTime createdAt,
        double maxDiscountAmount
) {}