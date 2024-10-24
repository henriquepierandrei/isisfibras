package com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto;

import com.pierandrei.isisfibras.Enuns.CategoriesEnum;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ProductUpdateDto(
        String sku,
        String name,
        String description,
        List<String> imageUrlsProduct,
        String imageUrlPrincipal,
        int quantity,
        double price,
        CategoriesEnum categoriesEnum,
        double weightProduct


) {
}
