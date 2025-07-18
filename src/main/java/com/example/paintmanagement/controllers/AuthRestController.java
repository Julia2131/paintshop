package com.example.paintmanagement.controllers;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.dto.*;
import com.example.paintmanagement.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final UserService userService;
    private final RoleService roleService;

    public AuthRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use.");
        }

        Role roleUser = roleService.findByName("ROLE_USER").orElseThrow();
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRoles(Set.of(roleUser));

        userService.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.findByEmail(request.getEmail())
                .map(user -> {
                    if (userService.getPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
                        return ResponseEntity.ok("Login successful.");
                    } else {
                        return ResponseEntity.status(401).body("Invalid password.");
                    }
                })
                .orElseGet(() -> ResponseEntity.status(404).body("User not found."));
    }
}