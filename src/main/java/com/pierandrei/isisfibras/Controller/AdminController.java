package com.pierandrei.isisfibras.Controller;

import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Exception.AuthExceptions.UserNotFoundException;
import com.pierandrei.isisfibras.Exception.UserNotUnauthorizedException;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Service.AdminServices.AdminService;
import com.pierandrei.isisfibras.Service.UserServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;



    @PutMapping("user/update/role/{id}")
    public ResponseEntity<String> updateRoleUser(@PathVariable(value = "id") UUID id,
                                                 @AuthenticationPrincipal UserModel userModel,
                                                 @RequestBody RolesUsers rolesUsers) throws UserNotFoundException {
        // Verifica se o usuário tem a permissão de ADMIN
        if (!userModel.getRolesUsers().equals(RolesUsers.ADMIN)) {
            throw new UserNotUnauthorizedException("Você não está autorizado para isso!");
        }

        Optional<UserModel> userModelOptional = this.adminService.getUser(id);
        if (userModelOptional.isEmpty()) {
            throw new UserNotFoundException("Usuário inexistente com o ID fornecido!");
        }

        // Gera código de acesso se o novo papel não for "USUARIO"
        if (!RolesUsers.USUARIO.equals(rolesUsers)) {
            this.adminService.generateCode(userModelOptional.get().getId());
        }

        this.adminService.changeRole(userModelOptional.get().getId(), id, rolesUsers);
        return ResponseEntity.ok("Usuário ID: " + userModelOptional.get().getId() + " foi atualizado para: " + rolesUsers + "!");
    }



}
