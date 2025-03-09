package com.example.chat_app_api.services;

import com.example.chat_app_api.dto.response.auth.UserResponse;
import com.example.chat_app_api.entitys.User;
import com.example.chat_app_api.exceptions.AppException;
import com.example.chat_app_api.exceptions.ErrorCode;
import com.example.chat_app_api.mapper.UserMapper;
import com.example.chat_app_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }
}
