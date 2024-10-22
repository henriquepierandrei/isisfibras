package com.pierandrei.isisfibras.Repository;

import com.pierandrei.isisfibras.Model.LogisticModels.ProductsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductsModel, String> {
    Optional<ProductsModel> findBySku(String skuProduct);
}
