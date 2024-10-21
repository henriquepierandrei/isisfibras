package com.pierandrei.isisfibras.Exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotUnauthorizedException extends AuthenticationException {
    public UserNotUnauthorizedException(final String msg) {
        super(msg);
    }
}
