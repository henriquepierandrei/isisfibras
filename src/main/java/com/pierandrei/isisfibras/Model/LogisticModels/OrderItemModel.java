package com.pierandrei.isisfibras.Model.LogisticModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class OrderItemModel {

    @Id
    private String skuProduct; // SKU do produto

    private int quantity; // Quantidade do produto no pedido

    private double price; // Preço unitário do produto
}
