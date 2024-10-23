package com.pierandrei.isisfibras.Model.LogisticModels;

import com.pierandrei.isisfibras.Model.UserModels.AddressModel;
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
    private List<ProductOrder> orderItems;

    private double totalPrice;

    private UUID idUserOwner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AddressModel addressModel;


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
