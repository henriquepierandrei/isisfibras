package com.pierandrei.isisfibras.Infra.Security;

import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final Authenticate authentication;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recoverToken(request);
        var email = tokenService.validateToken(token);

        if (email != null) {
            UserModel user = authentication.loadUserByEmail(email);
            var authorities = authentication.getAuthoritiesForUser(user);

            var authToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            // Se o token não for válido, retorna um erro 401 Unauthorized
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro com a validação do token!");
            return; // Interrompe a execução do filtro
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
