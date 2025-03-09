package com.example.chat_app_api.services;

import com.example.chat_app_api.dto.request.auth.LoginRequest;
import com.example.chat_app_api.dto.request.auth.RefreshTokenRequest;
import com.example.chat_app_api.dto.request.auth.RegisterRequest;
import com.example.chat_app_api.dto.response.auth.LoginResponse;
import com.example.chat_app_api.dto.response.auth.RefreshTokenResponse;
import com.example.chat_app_api.dto.response.auth.UserResponse;
import com.example.chat_app_api.entitys.InvalidToken;
import com.example.chat_app_api.entitys.User;
import com.example.chat_app_api.exceptions.AppException;
import com.example.chat_app_api.exceptions.ErrorCode;
import com.example.chat_app_api.mapper.UserMapper;
import com.example.chat_app_api.model.UserDetail;
import com.example.chat_app_api.repository.UserRepository;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    JwtGeneratorService jwtGeneratorService;
    InvalidTokenService invalidTokenService;
    AuthenticationManager authenticationManager;

    public UserResponse register(RegisterRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user != null) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User userRegister = userMapper.toUser(request);
        userRegister.setPassword(passwordEncoder.encode(request.getPassword()));
        userRegister.setUserId(UUID.randomUUID().toString());
        userRepository.save(userRegister);

        return userMapper.toUserResponse(userRegister);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();

        User user = userMapper.toUser(userDetail);

        String accessToken = jwtGeneratorService.generateAccessToken(user);
        String refreshToken = jwtGeneratorService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String authHeader) {
        try {
            if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            String token = authHeader.substring("Bearer ".length());
            SignedJWT signedJWT = jwtGeneratorService.verifyToken(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String jwtId = claimsSet.getJWTID();

            InvalidToken invalidToken = InvalidToken.builder()
                    .tokenId(jwtId)
                    .expiredTime(claimsSet.getExpirationTime().toInstant())
                    .build();

            invalidTokenService.save(invalidToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.LOGOUT_FAILED);
        }
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            SignedJWT signedJWT = jwtGeneratorService.verifyToken(refreshToken);
            int userId = jwtGeneratorService.extractUserId(refreshToken);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            boolean isRefreshToken = signedJWT.getJWTClaimsSet().getBooleanClaim("isRefreshToken");
            if (!isRefreshToken) {
                throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
            }
            String accessToken = jwtGeneratorService.generateAccessToken(user);
            return RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
        }
    }
}
