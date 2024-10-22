package com.pierandrei.isisfibras.Model.UserModels;

import com.pierandrei.isisfibras.Model.LogisticModels.CouponModel;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductOrder;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductsModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class CartModel {
    @Id
    private UUID idUser;

    // Lista de produtos adicionados ao carrinho
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProductOrder> productOrders = new ArrayList<>();

    @NotBlank
    private double totalPrice;  // Preço total do carrinho

    private double totalDiscount;  // Desconto total aplicado

    private double subTotal;  // Subtotal antes dos descontos

    private int totalItems;  // Total de itens no carrinho

    private boolean checkOut;  // Flag para indicar se o carrinho foi finalizado

    private LocalDateTime createdAt;  // Data de criação do carrinho

    private LocalDateTime updatedAt;  // Data de última atualização do carrinho


    // Relacionamento com cupons
    @ManyToMany
    private List<CouponModel> appliedCoupons;
}
