package com.pierandrei.isisfibras.Controller;

import com.pierandrei.isisfibras.Exception.AuthExceptions.CodeNotExistsException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneExistsException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneNotFoundException;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Service.MessageSenderService.EmailService;
import com.pierandrei.isisfibras.Service.MessageSenderService.TwilioService;
import com.pierandrei.isisfibras.Service.UserServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;


    @PreAuthorize("hasRole('USER'")
    @GetMapping("/send")
    public ResponseEntity<String> sendTest(@RequestParam("phone") String phone, @AuthenticationPrincipal UserModel userModel) {
        try {
            // Chama o método sendMessage e retorna uma resposta HTTP 200 em caso de sucesso
            this.service.generateCode(userModel.getId(), phone);
            return ResponseEntity.ok("Mensagem enviada com sucesso!");
        } catch (PhoneNotFoundException e) {
            // Retorna uma resposta HTTP 400 se o número de telefone não for encontrado
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Retorna uma resposta HTTP 500 em caso de erro inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER'")
    @PostMapping("/addphone")
    public ResponseEntity<String> addPhone(@RequestParam("code") String code, @AuthenticationPrincipal UserModel userModel) {
        try {
            // Chama o serviço para adicionar o telefone
            String response = this.service.addPhoneForUser(code, userModel.getId());
            return ResponseEntity.ok(response); // Retorna 200 OK com a mensagem de sucesso
        } catch (UserPrincipalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retorna 404 se o usuário não for encontrado
        } catch (PhoneExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Retorna 409 se o telefone já existir
        } catch (CodeNotExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Retorna 400 se o código estiver incorreto
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao processar a solicitação."); // Retorna 500 para qualquer outro erro
        }
    }


}
