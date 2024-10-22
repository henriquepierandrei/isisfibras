package com.pierandrei.isisfibras.Repository;

import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByEmail(String email);

    boolean existsByCpf(String cpf);

    Page<UserModel> findByRolesUsers(RolesUsers rolesUsers, Pageable pageable);

    List<UserModel> findByReceivePromotions(boolean b);
}
