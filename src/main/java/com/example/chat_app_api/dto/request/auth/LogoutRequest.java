package com.example.chat_app_api.dto.request.auth;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogoutRequest {
    private String token;
}
