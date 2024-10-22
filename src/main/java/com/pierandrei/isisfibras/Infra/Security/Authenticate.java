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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Authenticate {
    private final UserRepository userRepository;

    public UserModel loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }

    public List<SimpleGrantedAuthority> getAuthorities(UserModel user) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (user.getRolesUsers() == RolesUsers.USUARIO) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO"));
        }
        if (user.getRolesUsers() == RolesUsers.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (user.getRolesUsers() == RolesUsers.SUPORTE) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPORTE"));
        }
        if (user.getRolesUsers() == RolesUsers.GERENTE_LOGISTICO) {
            authorities.add(new SimpleGrantedAuthority("ROLE_GERENTE_LOGISTICO"));
        }
        if (user.getRolesUsers() == RolesUsers.OPERADOR) {
            authorities.add(new SimpleGrantedAuthority("ROLE_OPERADOR"));
        }



        return authorities;
    }
}
