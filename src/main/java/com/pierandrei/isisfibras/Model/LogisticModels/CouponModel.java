package com.pierandrei.isisfibras.Model.LogisticModels;

import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
public class CouponModel {
    @Id
    private String code;

    private String description;    // Descrição do cupom

    private int valuePerCentDiscount;  // Valor percentual de desconto (0 a 100)

    private boolean freeShipping;  // Indica se o frete é gratuito

    private double minimumAmount;  // Valor mínimo para que o cupom seja aplicável

    private boolean couponActive;  // Indica se o cupom está ativo ou não

    private LocalDate expirationDate;  // Data de expiração do cupom

    private int usageLimit;  // Número máximo de usos do cupom

    private boolean singleUse;  // Se o cupom pode ser utilizado apenas uma vez por cliente

    private LocalDateTime createdAt;  // Data de criação do cupom

    private double maxDiscountAmount;  // Valor máximo de desconto aplicável

    private List<UUID> idUsersUsed;
}
