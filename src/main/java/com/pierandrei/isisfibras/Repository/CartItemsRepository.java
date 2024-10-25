package com.pierandrei.isisfibras.Repository;

import com.pierandrei.isisfibras.Model.UserModels.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
}
