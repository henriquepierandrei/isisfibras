package com.pierandrei.isisfibras.Dto.Admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CouponCreateDto(
        String couponCode,
        String description,
        int valuePerCentDiscount,
        boolean freeShipping,
        double minimumAmount,
        boolean couponActive,
        LocalDate expirationDate,
        int usageLimit,
        boolean singleUse,
        double maxDiscountAmount
) {}