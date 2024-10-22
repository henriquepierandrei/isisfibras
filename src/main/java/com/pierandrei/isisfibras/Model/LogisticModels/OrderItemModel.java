package com.pierandrei.isisfibras.Model.LogisticModels;

import jakarta.persistence.*;
import lombok.Data;

@Entity // Alterado para @Entity
@Data
public class OrderItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Se você quiser que o ID seja gerado automaticamente
    private Long id; // Adicionando um ID único para a entidade

    private String skuProduct; // SKU do produto

    private int quantity; // Quantidade do produto no pedido

    private double price; // Preço unitário do produto

    @ManyToOne // Definindo o relacionamento com OrdersModel
    @JoinColumn(name = "order_id", nullable = false) // Chave estrangeira
    private OrdersModel order; // Relacionamento com o pedido
}
