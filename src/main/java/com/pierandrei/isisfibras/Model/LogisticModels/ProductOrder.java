package com.pierandrei.isisfibras.Model.LogisticModels;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Embeddable
@Data
public class ProductOrder {

    private String name;
    private int quantity;
    private double price;
}
