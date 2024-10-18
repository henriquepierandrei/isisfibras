package com.pierandrei.isisfibras.Model.LogisticModels;

import com.pierandrei.isisfibras.Enuns.CategoriesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ProductsModel {
    @Id
    private String sku;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private List<String> imagesUrls;

    @NotBlank
    private int quantity;

    @NotBlank
    private double price;

    @NotBlank
    private String size;

    @NotBlank
    private double weight;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private CategoriesEnum categoriesEnum;

    private boolean inStock;

    private double discountPrice;

    private int daysGuarantee;








}
