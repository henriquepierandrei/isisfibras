package com.pierandrei.isisfibras.Infra.Security;

import com.pierandrei.isisfibras.Enuns.RolesEmployee;
import com.pierandrei.isisfibras.Model.EmployeeModels.EmployeeModel;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.EmployeeRepository;
import com.pierandrei.isisfibras.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Authenticate {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeModel loadEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário/Senha incorretos!"));
    }

    public UserModel loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário/Senha incorretos!"));
    }

    public List<SimpleGrantedAuthority> getAuthorities(EmployeeModel employeeModel, UserModel userModel) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        RolesEmployee position = employeeModel.getPosition();
        if (position != null) {
            switch (position) {
                case OPERADOR -> authorities.add(new SimpleGrantedAuthority("ROLE_OPERADOR"));
                case EMBALADOR -> authorities.add(new SimpleGrantedAuthority("ROLE_EMBALADOR"));
                case GERENTE_LOGISTICO -> authorities.add(new SimpleGrantedAuthority("ROLE_GERENTE_LOGISTICO"));
                case SUPORTE -> authorities.add(new SimpleGrantedAuthority("ROLE_SUPORTE"));
                default -> throw new IllegalArgumentException("Posição não reconhecida: " + position);
            }
        }

        return authorities;
    }
}
