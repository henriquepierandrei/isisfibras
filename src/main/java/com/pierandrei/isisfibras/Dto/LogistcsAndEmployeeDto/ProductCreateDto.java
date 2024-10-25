package com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto;

import com.pierandrei.isisfibras.Enuns.CategoriesEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProductCreateDto(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull List<String> imageUrlsProduct,
        @NotNull int quantity,
        @NotNull double price,
        @NotNull CategoriesEnum categoriesEnum,
        @NotNull double weightProduct,
        @NotNull int daysGuarantee


        ) {
}
