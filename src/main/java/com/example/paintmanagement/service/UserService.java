package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> findByEmail(String email);
    User getById(Long id);
    PasswordEncoder getPasswordEncoder();
}