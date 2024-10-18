package com.pierandrei.isisfibras.Repository;

import com.pierandrei.isisfibras.Model.EmployeeModels.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeModel, UUID> {
    Optional<EmployeeModel> findByUsername(String username);
}
