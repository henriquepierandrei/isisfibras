package com.pierandrei.isisfibras.Model.LogisticModels;

import com.pierandrei.isisfibras.Model.UserModels.UserModel;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemModel> orderItems;

    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserModel buyer; // Altere para 'buyer' se estiver usando esse nome

    @ManyToOne // Relacionamento com UserModel
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel userModel; // Adicionando o relacionamento com UserModel

    private LocalDateTime buyedAt;

    private String trackingCode;

    private boolean freeShipping;

    private LocalDateTime deliveredAt;

    @ManyToOne
    private CouponModel coupon;

    private String orderCode;

    private double discountPrice;

    private boolean haveCoupon;
}
