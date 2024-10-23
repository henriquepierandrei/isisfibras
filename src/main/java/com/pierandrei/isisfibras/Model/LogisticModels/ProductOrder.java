package com.pierandrei.isisfibras.Model.LogisticModels;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Id para a entidade

    private String sku;
    private String imagePrincipalUrl;
    private String name;
    private int quantity;
    private double price;

    @ManyToOne // Relação com a entidade Order
    private OrdersModel order; // Supondo que você tenha uma entidade Order
}
