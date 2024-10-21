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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // Login do usuario
    private LoginResponse loginAuth(String email, String password){
        Optional<UserModel> userModelOptional = this.userRepository.findByEmail(email);
        if (userModelOptional.isEmpty() || !passwordEncoder.matches(password, userModelOptional.get().getPassword())) {
            throw new BadCredentialsException("Usuário não encontrado!");
        }
        String token = tokenService.generateToken(userModelOptional.get());
        return new LoginResponse(token, userModelOptional.get().getName(), userModelOptional.get().getId());

    }



    // Registro do usuario
    private RegisterResponse registerAuth(RegisterDto registerDto){
        Optional<UserModel> userModelOptional = this.userRepository.findByEmail(registerDto.email());
        if (userModelOptional.isPresent()) throw new UserAlreadyExistAuthenticationException("Esse email já está em uso!");

        if (this.userRepository.existsByCpf(registerDto.cpf())) throw new UserAlreadyExistAuthenticationException("Esse CPF já está em uso!");

        UserModel userModel = new UserModel();
        userModel.setName(registerDto.name());
        userModel.setEmail(registerDto.email());
        userModel.setPassword(passwordEncoder.encode(registerDto.password()));
        userModel.set
    }
}
