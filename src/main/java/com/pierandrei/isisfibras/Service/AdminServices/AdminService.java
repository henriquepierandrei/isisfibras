package com.pierandrei.isisfibras.Service.AdminServices;

import com.pierandrei.isisfibras.Dto.Admin.PaginatedUsersResponse;
import com.pierandrei.isisfibras.Dto.Admin.UsersByRoleResponse;
import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Exception.AuthExceptions.UserNotFoundException;
import com.pierandrei.isisfibras.Exception.UserNotUnauthorizedException;
import com.pierandrei.isisfibras.Model.Historic.RoleHistoricChange;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.RoleHistoricChangeRepository;
import com.pierandrei.isisfibras.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RoleHistoricChangeRepository roleHistoricChangeRepository;

    // Definir ROLE ao usuário
    public String changeRole(UUID idAdmin, UUID idUser, RolesUsers rolesUsers) {
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
    public String deleteUser(UUID idAdmin, UUID idUser) throws UserNotFoundException {
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


    // Obter Lista de usuários de acordo com sua ROLE
    public PaginatedUsersResponse getUsersByRole(RolesUsers rolesUsers, int page, int size) {
        // Cria um objeto Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Busca a página de usuários com a ROLE especificada
        Page<UserModel> userPage = this.userRepository.findByRolesUsers(rolesUsers, pageable);

        // Mapeia os usuários encontrados para o formato de resposta desejado
        List<UsersByRoleResponse> usersByRoleResponses = userPage.getContent()
                .stream()
                .map(user -> new UsersByRoleResponse(user.getId(), user.getEmail(), user.getPhone(),  user.getRolesUsers()))
                .collect(Collectors.toList());

        // Retorna a resposta paginada
        return new PaginatedUsersResponse(usersByRoleResponses, userPage.getTotalPages(), userPage.getTotalElements());
    }


    // Obter Lista de usuários de acordo com

}
