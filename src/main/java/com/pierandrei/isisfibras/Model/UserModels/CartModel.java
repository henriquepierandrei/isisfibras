package com.pierandrei.isisfibras.Model.UserModels;

import com.pierandrei.isisfibras.Model.LogisticModels.CouponModel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class CartModel {
    @Id
    private UUID idUser;

    // Lista de produtos adicionados ao carrinho
    @ElementCollection
    private List<CartItems> cartItems;


    private double totalPrice;  // Preço total do carrinho

    private double totalDiscount;  // Desconto total aplicado

    private double subTotal;  // Subtotal antes dos descontos

    private int totalItems;  // Total de itens no carrinho


    private LocalDateTime createdAt;  // Data de criação do carrinho

    private LocalDateTime updatedAt;  // Data de última atualização do carrinho


    // Relacionamento com cupons
    @ManyToMany
    private List<CouponModel> appliedCoupons;
}
