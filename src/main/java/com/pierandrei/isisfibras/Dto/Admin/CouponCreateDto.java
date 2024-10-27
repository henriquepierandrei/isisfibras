package com.pierandrei.isisfibras.Dto.Admin;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CouponCreateDto(
        @NotBlank String couponCode,
        @NotBlank String description,
        int valuePerCentDiscount,
        boolean freeShipping,
        double minimumAmount,
        boolean couponActive,
        LocalDate expirationDate,
        int usageLimit,
        boolean singleUse,
        double maxDiscountAmount
) {}