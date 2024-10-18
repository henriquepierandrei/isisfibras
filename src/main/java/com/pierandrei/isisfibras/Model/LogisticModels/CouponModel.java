package com.pierandrei.isisfibras.Model.LogisticModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CouponModel {
    @Id
    private String couponCode;  // Código exclusivo do cupom

    private String name;  // Nome descritivo do cupom

    private String typeDiscount;  // Percentual ou valor fixo

    private double valuePerCentDiscount;  // Valor percentual de desconto (0 a 100)

    private boolean freeShipping;  // Indica se o frete é gratuito

    private double minimumAmount;  // Valor mínimo para que o cupom seja aplicável

    private boolean couponActive;  // Indica se o cupom está ativo ou não

    private LocalDateTime expirationDate;  // Data de expiração do cupom

    private int usageLimit;  // Número máximo de usos do cupom

    private boolean singleUse;  // Se o cupom pode ser utilizado apenas uma vez por cliente

    private LocalDateTime createdAt;  // Data de criação do cupom

    private double maxDiscountAmount;  // Valor máximo de desconto aplicável
}
