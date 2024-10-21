package com.pierandrei.isisfibras.Service;

import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Exception.Auth.UserNotFoundException;
import com.pierandrei.isisfibras.Exception.UserNotUnauthorizedException;
import com.pierandrei.isisfibras.Model.Historic.RoleHistoricChange;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.RoleHistoricChangeRepository;
import com.pierandrei.isisfibras.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RoleHistoricChangeRepository roleHistoricChangeRepository;

    // Definir ROLE ao usuário
    private String changeRole(UUID idAdmin, UUID idUser, RolesUsers rolesUsers) {
        Optional<UserModel> userModelOptional = this.userRepository.findById(idUser);
        if (userModelOptional.isEmpty()) {
            throw new BadCredentialsException("Usuário inexistente!");
        }

        Optional<UserModel> adminModelOptional = this.userRepository.findById(idAdmin);
        if (adminModelOptional.isEmpty()) {
            throw new BadCredentialsException("Admin inexistente!");
        }

        // Verifica se o administrador tem a permissão
        if (adminModelOptional.get().getRolesUsers() == RolesUsers.ADMIN) {
            UserModel userModel = userModelOptional.get();
            RolesUsers oldRole = userModel.getRolesUsers();

            userModel.setRolesUsers(rolesUsers);
            this.userRepository.save(userModel);

            // Cria um histórico da mudança de papel
            RoleHistoricChange roleHistoricChange = new RoleHistoricChange();
            roleHistoricChange.setChangeAt(LocalDateTime.now());
            roleHistoricChange.setOldRolesUsers(oldRole);
            roleHistoricChange.setNewRolesUsers(rolesUsers);
            roleHistoricChange.setRoleUserChanged(idUser);
            roleHistoricChange.setIdAdminOwnerChange(idAdmin);
            this.roleHistoricChangeRepository.save(roleHistoricChange);
        } else {
            throw new UserNotUnauthorizedException("Você não tem permissão para alteração!");
        }

        return "Posição mudada para: " + rolesUsers;
    }


    // Deletar Usuário no Banco de Dados
    private String deleteUser(UUID idAdmin, UUID idUser) throws UserNotFoundException {
        // Verifica se o usuário a ser deletado existe
        Optional<UserModel> userModelOptional = this.userRepository.findById(idUser);
        if (userModelOptional.isEmpty()) {
            throw new UserNotFoundException("Usuário inexistente!");
        }

        // Verifica se o administrador existe
        Optional<UserModel> adminModelOptional = this.userRepository.findById(idAdmin);
        if (adminModelOptional.isEmpty()) {
            throw new UserNotFoundException("Admin inexistente!");
        }

        // Verifica se o administrador tem a permissão para deletar
        if (adminModelOptional.get().getRolesUsers() != RolesUsers.ADMIN) {
            throw new UserNotUnauthorizedException("Você não tem permissão para deletar usuários!");
        }

        // Realiza a deleção
        this.userRepository.delete(userModelOptional.get());

        return "Usuário: ID: [" + userModelOptional.get().getId() + "] removido!";
    }


}
