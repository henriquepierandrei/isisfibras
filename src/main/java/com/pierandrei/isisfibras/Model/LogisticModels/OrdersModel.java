package com.pierandrei.isisfibras.Model.LogisticModels;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class OrdersModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idOrder;

    @OneToMany
    private List<OrderItemModel> orderItems;

    private double totalPrice;

    private UUID idBuyer; // ID do comprador (pode referenciar o `UserModel`)

    private LocalDateTime buyedAt; // Data da compra

    private String trackingCode; // Código de rastreamento

    private boolean freeShipping; // Indica se o frete é gratuito

    private LocalDateTime deliveredAt; // Data de entrega

    @ManyToOne
    private CouponModel coupon; // Relacionamento com o cupom aplicado

    private String orderCode; // Código da ordem

    private double discountPrice; // Preço com desconto aplicado

    private boolean haveCoupon; // Indica se há um cupom aplicado
}
