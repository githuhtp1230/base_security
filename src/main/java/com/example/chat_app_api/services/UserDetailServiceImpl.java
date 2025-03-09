package com.example.chat_app_api.services;

import com.example.chat_app_api.entitys.User;
import com.example.chat_app_api.exceptions.AppException;
import com.example.chat_app_api.exceptions.ErrorCode;
import com.example.chat_app_api.mapper.UserMapper;
import com.example.chat_app_api.model.UserDetail;
import com.example.chat_app_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if ( user == null ) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        return userMapper.toUserDetail(user);
    }
}
