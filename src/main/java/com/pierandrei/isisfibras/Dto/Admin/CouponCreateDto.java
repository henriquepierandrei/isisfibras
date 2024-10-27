package com.pierandrei.isisfibras.Dto.Admin;

import java.time.LocalDateTime;

public record CouponCreateDto(
        String couponCode,
        String description,
        double valuePerCentDiscount,
        boolean freeShipping,
        double minimumAmount,
        boolean couponActive,
        LocalDateTime expirationDate,
        int usageLimit,
        boolean singleUse,
        double maxDiscountAmount
) {}