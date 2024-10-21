package com.pierandrei.isisfibras.Infra.Security;

import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
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
    private final UserRepository userRepository;

    // Carrega User por email
    public UserModel loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário/Senha incorretos!"));
    }

    // Autoridades para o User
    public List<SimpleGrantedAuthority> getAuthoritiesForUser(UserModel userModel) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        RolesUsers position = userModel.getRolesUsers();
        if (position != null) {
            switch (position) {
                case USUARIO -> authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO"));
                case OPERADOR -> authorities.add(new SimpleGrantedAuthority("ROLE_OPERADOR"));
                case EMBALADOR -> authorities.add(new SimpleGrantedAuthority("ROLE_EMBALADOR"));
                case GERENTE_LOGISTICO -> authorities.add(new SimpleGrantedAuthority("ROLE_GERENTE_LOGISTICO"));
                case SUPORTE -> authorities.add(new SimpleGrantedAuthority("ROLE_SUPORTE"));
                case ADMIN -> authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                default -> throw new IllegalArgumentException("Posição não reconhecida: " + position);
            }
        }

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
