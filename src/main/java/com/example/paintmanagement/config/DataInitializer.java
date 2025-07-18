package com.example.paintmanagement.config;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.repository.*;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class DataInitializer {
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public DataInitializer(RoleRepository rr, UserRepository ur, PasswordEncoder enc) {
        this.roleRepo = rr;
        this.userRepo = ur;
        this.encoder = enc;
    }

    @PostConstruct
    public void init() {
        // Nếu chưa có ROLE_ADMIN, thêm
        roleRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepo.save(new Role(null, "ROLE_ADMIN")));
        // Nếu chưa có ROLE_USER, thêm
        roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> roleRepo.save(new Role(null, "ROLE_USER")));

        // Tạo admin nếu cần
        if (!userRepo.findByEmail("admin@example.com").isPresent()) {
            Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));
            userRepo.save(admin);
        }
    }


}