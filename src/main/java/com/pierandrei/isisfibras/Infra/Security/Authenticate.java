package com.pierandrei.isisfibras.Infra.Security;

import com.pierandrei.isisfibras.Enuns.RolesEmployee;
import com.pierandrei.isisfibras.Model.EmployeeModels.EmployeeModel;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.EmployeeRepository;
import com.pierandrei.isisfibras.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Authenticate implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    // Carrega Employee por username
    public EmployeeModel loadEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário/Senha incorretos!"));
    }

    // Carrega User por email
    public UserModel loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário/Senha incorretos!"));
    }

    // Autoridades para o Employee
    public List<SimpleGrantedAuthority> getAuthoritiesForEmployee(EmployeeModel employeeModel) {
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

    // Autoridades para o User
    public List<SimpleGrantedAuthority> getAuthoritiesForUser(UserModel userModel) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Carrega o User por email
        UserModel user = loadUserByEmail(email);
        // Obtém as autoridades
        var authorities = getAuthoritiesForUser(user);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
