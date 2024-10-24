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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;



    @PutMapping("user/update/role/{id}")
    public ResponseEntity updateRoleUser(@PathVariable(value = "id") UUID id, @AuthenticationPrincipal UserModel userModel, RolesUsers rolesUsers) throws UserNotFoundException {
        if (userModel.getRolesUsers() != RolesUsers.ADMIN) {
            throw new UserNotUnauthorizedException("Você não está autorizado para isso!");
        }

        Optional<UserModel> userModelOptional = this.adminService.getUser(id);
        if (userModelOptional.isEmpty()){
            throw new UserNotFoundException("Usuário inexistente com o ID fornecido!");
        }

        this.adminService.changeRole(userModelOptional.get().getId(), id, rolesUsers);
        return ResponseEntity.ok("Usuário ID: "+userModelOptional.get().getId()+" foi atualizado para: "+rolesUsers+"!");
    }


}
