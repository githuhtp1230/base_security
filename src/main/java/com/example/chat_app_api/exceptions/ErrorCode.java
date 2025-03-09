package com.example.chat_app_api.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    EMAIL_INVALID(HttpStatus.BAD_REQUEST, "Email is invalid"),
    USERNAME_INVALID(HttpStatus.BAD_REQUEST, "Username is invalid"),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "Password is invalid"),
    EMAIL_EXISTED(HttpStatus.CONFLICT, "Email already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "You have not permission"),
    INTERAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "Token is invalid"),
    REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "Refresh token is invalid"),
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST, "Logout failed");

    private HttpStatusCode statusCode;
    private String message;
}
