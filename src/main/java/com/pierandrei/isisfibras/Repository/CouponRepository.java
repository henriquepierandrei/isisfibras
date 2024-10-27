package com.pierandrei.isisfibras.Repository;

import com.pierandrei.isisfibras.Model.LogisticModels.CouponModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponModel, String> {
    boolean existsByCode(String name);
}
