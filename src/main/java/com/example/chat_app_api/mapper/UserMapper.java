package com.example.chat_app_api.mapper;

import com.example.chat_app_api.dto.request.auth.RegisterRequest;
import com.example.chat_app_api.dto.response.auth.UserResponse;
import com.example.chat_app_api.entitys.User;
import com.example.chat_app_api.model.UserDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);
    User toUser(UserDetail userDetail);

    UserResponse toUserResponse(User user);
    UserResponse toUserResponse(UserDetail userDetail);

    UserDetail toUserDetail(User user);
}
