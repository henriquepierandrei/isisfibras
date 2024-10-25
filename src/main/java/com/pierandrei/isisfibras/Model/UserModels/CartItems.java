package com.pierandrei.isisfibras.Model.UserModels;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CartItems {
    @Id
    @GeneratedValue
    private Long id;

    private String sku;
    private int quantity;

}
