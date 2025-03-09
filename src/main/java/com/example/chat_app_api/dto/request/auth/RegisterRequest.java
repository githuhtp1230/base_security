package com.example.chat_app_api.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
    private String username;
    @Email(message = "Email is invalid")
    private String email;
    private String password;
}
