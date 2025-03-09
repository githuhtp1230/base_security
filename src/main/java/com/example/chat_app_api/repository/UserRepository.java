package com.example.chat_app_api.repository;

import com.example.chat_app_api.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findUserByEmailAndPassword(String email, String password);
}
