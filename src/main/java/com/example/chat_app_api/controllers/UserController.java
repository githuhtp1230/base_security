package com.example.chat_app_api.controllers;

import com.example.chat_app_api.dto.ApiResponse;
import com.example.chat_app_api.dto.response.auth.UserResponse;
import com.example.chat_app_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable Integer userId) {
        UserResponse userResponse = userService.getUserById(userId);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User found")
                .data(userResponse)
                .build();
        return apiResponse;
    }
}
