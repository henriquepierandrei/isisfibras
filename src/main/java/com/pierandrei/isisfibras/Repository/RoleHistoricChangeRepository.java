package com.pierandrei.isisfibras.Repository;

import com.pierandrei.isisfibras.Model.Historic.RoleHistoricChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleHistoricChangeRepository extends JpaRepository<RoleHistoricChange, UUID> {
}
