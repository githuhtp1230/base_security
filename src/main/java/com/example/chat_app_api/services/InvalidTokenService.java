package com.example.chat_app_api.services;

import com.example.chat_app_api.entitys.InvalidToken;
import com.example.chat_app_api.repository.InvalidTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvalidTokenService {
    private final InvalidTokenRepository invalidTokenRepository;

    public void save(InvalidToken invalidToken) {
        invalidTokenRepository.save(invalidToken);
    }

    public boolean isTokenExists(String tokenId) {
        return invalidTokenRepository.existsByTokenId(tokenId);
    }
}
