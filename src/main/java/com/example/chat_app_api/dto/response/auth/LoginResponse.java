package com.example.chat_app_api.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
}
