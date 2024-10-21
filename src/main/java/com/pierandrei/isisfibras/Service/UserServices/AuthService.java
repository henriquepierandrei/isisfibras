package com.pierandrei.isisfibras.Service.UserServices;

import com.pierandrei.isisfibras.Dto.Auth.LoginResponse;
import com.pierandrei.isisfibras.Dto.Auth.RegisterDto;
import com.pierandrei.isisfibras.Dto.Auth.RegisterResponse;
import com.pierandrei.isisfibras.Exception.Auth.UserAlreadyExistAuthenticationException;
import com.pierandrei.isisfibras.Infra.Security.TokenService;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // Login do usuario
    public LoginResponse loginAuth(String email, String password) {
        Optional<UserModel> userModelOptional = this.userRepository.findByEmail(email);
        if (userModelOptional.isEmpty() || !passwordEncoder.matches(password, userModelOptional.get().getPassword())) {
            throw new BadCredentialsException("Usuário não encontrado!");
        }
        String token = tokenService.generateToken(userModelOptional.get());
        return new LoginResponse(token, userModelOptional.get().getName(), userModelOptional.get().getId(), "Login bem-sucedido!");
    }

    // Registro do usuario
    public RegisterResponse registerAuth(RegisterDto registerDto) {
        // Verifica se o CPF é válido
        if (!isCpfValid(registerDto.cpf())) {
            throw new UserAlreadyExistAuthenticationException("CPF inválido!");
        }

        Optional<UserModel> userModelOptional = this.userRepository.findByEmail(registerDto.email());
        if (userModelOptional.isPresent()) throw new UserAlreadyExistAuthenticationException("Esse email já está em uso!");

        if (this.userRepository.existsByCpf(registerDto.cpf())) throw new UserAlreadyExistAuthenticationException("Esse CPF já está em uso!");

        UserModel userModel = new UserModel();
        userModel.setName(registerDto.name());
        userModel.setEmail(registerDto.email());
        userModel.setPassword(passwordEncoder.encode(registerDto.password()));
        userModel.setDateBorn(registerDto.dateBorn());
        userModel.setCpf(registerDto.cpf());
        userModel.setCreatedAt(LocalDateTime.now());
        this.userRepository.save(userModel);

        String token = tokenService.generateToken(userModel);

        return new RegisterResponse(registerDto.name(), userModel.getId(),userModel.getEmail(),"Registro bem-sucedido!", token);
    }

    // Método para validar CPF
    private boolean isCpfValid(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // CPF deve ter 11 dígitos
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false; // CPF inválido
        }

        // Cálculo do primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (10 - i) * (cpf.charAt(i) - '0');
        }
        int firstDigit = (sum * 10) % 11;
        if (firstDigit == 10) {
            firstDigit = 0;
        }

        // Cálculo do segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (11 - i) * (cpf.charAt(i) - '0');
        }
        int secondDigit = (sum * 10) % 11;
        if (secondDigit == 10) {
            secondDigit = 0;
        }

        // Verifica se os dígitos verificadores estão corretos
        return (cpf.charAt(9) - '0' == firstDigit) && (cpf.charAt(10) - '0' == secondDigit);
    }
}
