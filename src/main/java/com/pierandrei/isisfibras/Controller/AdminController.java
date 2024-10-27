package com.pierandrei.isisfibras.Controller;

import com.pierandrei.isisfibras.Dto.Admin.CouponCreateDto;
import com.pierandrei.isisfibras.Dto.Admin.CouponCreateResponse;
import com.pierandrei.isisfibras.Dto.Admin.UserResponse;
import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Exception.AuthExceptions.UserNotFoundException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.UserUnauthorizedException;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.CouponExistsException;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Service.AdminServices.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;


    // Obtém o usuário através do ID
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable(value = "id") UUID id) throws UserNotFoundException {
        UserResponse userResponse = this.adminService.getUserWithResponse(id);
        return ResponseEntity.ok(userResponse);
    }


    // Atualizar a posicação do usuário através do ID
    @PutMapping("user/update/role/{id}")
    public ResponseEntity<String> updateRoleUser(@PathVariable(value = "id") UUID id,
                                                 @AuthenticationPrincipal UserModel userModel,
                                                 @RequestParam RolesUsers rolesUsers) throws UserNotFoundException {
        // Verifica se o usuário tem a permissão de ADMIN
        if (!userModel.getRolesUsers().equals(RolesUsers.ADMIN)) {
            throw new UserUnauthorizedException("Você não está autorizado para isso!");
        }

        // Gera código de acesso se o novo papel não for "USUARIO"
        if (!RolesUsers.USUARIO.equals(rolesUsers)) {
            this.adminService.generateCode(id);
        }

        this.adminService.changeRole(userModel.getId(), id,  rolesUsers);
        return ResponseEntity.ok("Usuário ID: " + id + " foi atualizado para: " + rolesUsers + "!");
    }



    // Criar um Cupom
    public ResponseEntity<CouponCreateResponse> createCoupon(@AuthenticationPrincipal UserModel userModel,
                                                             @RequestBody CouponCreateDto couponCreateDto) throws CouponExistsException {
        CouponCreateResponse couponCreateResponse = this.adminService.createCoupon(couponCreateDto);
        return ResponseEntity.ok(couponCreateResponse);
    }



}
