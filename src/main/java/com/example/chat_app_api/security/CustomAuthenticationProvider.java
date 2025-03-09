package com.example.chat_app_api.security;

import com.example.chat_app_api.exceptions.AppException;
import com.example.chat_app_api.exceptions.ErrorCode;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("hello");
        throw new AppException(ErrorCode.ACCESS_DENIED);
//        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
