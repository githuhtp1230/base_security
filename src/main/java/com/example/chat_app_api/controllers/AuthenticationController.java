package com.example.chat_app_api.controllers;

import com.example.chat_app_api.dto.ApiResponse;
import com.example.chat_app_api.dto.request.auth.LogoutRequest;
import com.example.chat_app_api.dto.request.auth.RefreshTokenRequest;
import com.example.chat_app_api.dto.request.auth.LoginRequest;
import com.example.chat_app_api.dto.request.auth.RegisterRequest;
import com.example.chat_app_api.dto.response.auth.LoginResponse;
import com.example.chat_app_api.dto.response.auth.RefreshTokenResponse;
import com.example.chat_app_api.dto.response.auth.UserResponse;
import com.example.chat_app_api.services.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("register")
    ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authenticationService.register(request);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Register successfully")
                .data(userResponse)
                .build();
        return apiResponse;
    }

    @PostMapping("login")
    ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authenticationService.login(request);
        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Login successfully")
                .data(loginResponse)
                .build();
        return apiResponse;
    }

    @PostMapping("logout")
    ApiResponse<?> logout(@RequestHeader("Authorization") String authHeader) {
        authenticationService.logout(authHeader);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(200)
                .message("Logout successfully")
                .build();
        return apiResponse;
    }

    @PostMapping("refresh-token")
    ApiResponse<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(request);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(200)
                .message("Refresh token successfully")
                .data(refreshTokenResponse)
                .build();
        return apiResponse;
    }

}
