package com.pierandrei.isisfibras.Model.LogisticModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class ProductReviewModel {
    @Id
    private String sku;  // Código identificador do produto

    private UUID idUserOwner;  // ID do usuário que fez a avaliação

    private String review;  // Texto da avaliação

    private int stars;  // Avaliação em estrelas (1-5)

    private String imageUrl;  // URL de imagens adicionadas na avaliação

    private LocalDateTime buyedAt;  // Data em que o produto foi comprado

    private LocalDateTime createdAt;  // Data de criação da avaliação

    private LocalDateTime updatedAt;  // Data da última atualização da avaliação

    private boolean verifiedPurchase;  // Indica se o usuário realmente comprou o produto

    private int helpfulVotes;  // Contador de votos úteis

    private String reply;  // Resposta da loja à avaliação, se aplicável

    private String status;  // Status da avaliação (ex: ACTIVE, PENDING, REMOVED)
}
