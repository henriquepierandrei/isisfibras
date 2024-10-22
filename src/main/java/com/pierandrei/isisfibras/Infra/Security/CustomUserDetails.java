package com.pierandrei.isisfibras.Infra.Security;

import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {
    private final Authenticate authentication;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = authentication.loadUserByEmail(email);
        var authorities = authentication.getAuthorities(user);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}