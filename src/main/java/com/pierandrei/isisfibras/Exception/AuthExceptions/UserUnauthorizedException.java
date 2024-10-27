package com.pierandrei.isisfibras.Exception.AuthExceptions;

import org.springframework.security.core.AuthenticationException;

public class UserUnauthorizedException extends AuthenticationException {
    public UserUnauthorizedException(final String msg) {
        super(msg);
    }
}
