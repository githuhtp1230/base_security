package com.example.chat_app_api.repository;

import com.example.chat_app_api.entitys.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Integer> {
    boolean existsByTokenId(String tokenId);
}
