package com.pierandrei.isisfibras.Dto.User;

import java.time.LocalDateTime;
import java.util.List;

public record CartResponse(
        List<ProductResponse> productResponseList,
        int totalPrice,
        boolean usedCoupon,
        String couponCode
) {
}
