package com.pierandrei.isisfibras.Model.LogisticModels;

import com.pierandrei.isisfibras.Enuns.CategoriesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private List<String> imagesUrls;  // URLs das imagens do produto

    private String imageUrlPrincipal;    // URL da imagem principal do produto

    @NotNull
    private int quantity;  // Quantidade disponível em estoque

    private boolean isExhausted;     // Se o produto está esgotado ( Ideal quando está sem estoque ou não se fabrica mais )

    @NotNull
    private double price;  // Preço do produto

    private int daysGuarantee;    // Dias de Garantia


    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoriesEnum categoriesEnum;  // Categoria do produto

    private boolean inStock;  // Indica se o produto está em estoque

    private double discountPrice;  // Preço com desconto (se aplicável)

    private int totalSellings=0;      // Quantidade de produto vendido

    private double averageRating=0;  // Média das avaliações de 1 a 5 estrelas


    @NotNull
    private double shippingWeight;  // Peso usado para cálculo de frete

    private LocalDateTime createdAt;  // Data em que o produto foi adicionado ao sistema

    private LocalDateTime updatedAt;  // Data da última atualização do produto

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductReviewModel> reviews; // Adicionando a lista de avaliações
}
