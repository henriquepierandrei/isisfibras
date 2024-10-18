package com.pierandrei.isisfibras.Model.UserModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class CartModel {
    @Id
    private UUID idUser;

    @NotBlank
    private double totalPrice;

    private double totalDiscount;

    private boolean checkOut;
}
