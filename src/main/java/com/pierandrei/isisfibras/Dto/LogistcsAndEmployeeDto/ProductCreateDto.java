package com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto;

import com.pierandrei.isisfibras.Enuns.CategoriesEnum;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ProductCreateDto(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank List<String> imageUrlsProduct,
        @NotBlank String imageUrlPrincipal,
        @NotBlank int quantity,
        @NotBlank double price,
        @NotBlank CategoriesEnum categoriesEnum,
        @NotBlank double weightProduct


        ) {
}
