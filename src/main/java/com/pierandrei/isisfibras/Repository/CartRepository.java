package com.pierandrei.isisfibras.Repository;

import com.pierandrei.isisfibras.Model.UserModels.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<CartModel, UUID> {
    CartModel findByIdUser(UUID id);
}
