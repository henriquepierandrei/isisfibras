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
    private String sku;

    private UUID idUserOwner;

    private String review;

    private int stars;

    private String imageUrl;

    private LocalDateTime buyedAt;
}
