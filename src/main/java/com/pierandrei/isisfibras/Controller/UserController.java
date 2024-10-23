package com.pierandrei.isisfibras.Controller;

import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneNotFoundException;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Service.MessageSenderService.EmailService;
import com.pierandrei.isisfibras.Service.MessageSenderService.TwilioService;
import com.pierandrei.isisfibras.Service.UserServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;


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

}
