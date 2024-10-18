package com.pierandrei.isisfibras.Model.LogisticModels;

import com.pierandrei.isisfibras.Enuns.CategoriesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ProductsModel {
    @Id
    private String sku;  // Código do produto (Stock Keeping Unit)

    @NotBlank
    private String name;  // Nome do produto

    @NotBlank
    private String description;  // Descrição detalhada do produto

    @NotBlank
    private List<String> imagesUrls;  // URLs das imagens do produto

    @NotBlank
    private int quantity;  // Quantidade disponível em estoque

    @NotBlank
    private double price;  // Preço do produto

    @NotBlank
    private String size;  // Tamanho (se aplicável, ex: vestuário, calçados)

    @NotBlank
    private double weight;  // Peso do produto

    @NotBlank
    @Enumerated(EnumType.STRING)
    private CategoriesEnum categoriesEnum;  // Categoria do produto

    private boolean inStock;  // Indica se o produto está em estoque

    private double discountPrice;  // Preço com desconto (se aplicável)

    private int daysGuarantee;  // Dias de garantia fornecidos


    private double averageRating;  // Média das avaliações de 1 a 5 estrelas

    private boolean isFeatured;  // Indica se o produto está em destaque

    private double shippingWeight;  // Peso usado para cálculo de frete

    private LocalDateTime createdAt;  // Data em que o produto foi adicionado ao sistema

    private LocalDateTime updatedAt;  // Data da última atualização do produto

    private List<ProductReviewModel> productReviews;  // Lista de avaliações do produto
}
