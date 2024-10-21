package com.pierandrei.isisfibras.Controller;

import com.pierandrei.isisfibras.Dto.Auth.LoginResponse;
import com.pierandrei.isisfibras.Dto.Auth.RegisterDto;
import com.pierandrei.isisfibras.Dto.Auth.RegisterResponse;
import com.pierandrei.isisfibras.Exception.Auth.UserAlreadyExistAuthenticationException;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.UserRepository;
import com.pierandrei.isisfibras.Service.UserServices.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestParam(value = "email") String email,
                                                   @RequestParam(value = "password") String password) {

        Optional<UserModel> userModelOptional = this.userRepository.findByEmail(email);
        userModelOptional.get().setLastLoginAt(LocalDateTime.now());
        this.userRepository.save(userModelOptional.get());


        try {
            // Chama o método de autenticação no serviço
            LoginResponse response = authService.loginAuth(email, password);

            // Retorna 200 OK com o objeto LoginResponse
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            // Se as credenciais estiverem erradas, retorna 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, "Credenciais erradas!")); // Ajuste se necessário para retorno vazio
        }
    }



    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        try {
            // Chama o método de registro no serviço
            RegisterResponse response = authService.registerAuth(registerDto);

            // Retorna 201 Created com o objeto RegisterResponse
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyExistAuthenticationException ex) {
            // Se o email ou CPF já existir, retorna 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RegisterResponse(null, null, null, "Email ou CPF já cadastrados", null)); // Ajuste conforme necessário
        }
    }


}
