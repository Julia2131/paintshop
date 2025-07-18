package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.User;
import com.example.paintmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> {
                    System.out.println("Loading role: " + role.getName()); // Debug từng role
                    return new SimpleGrantedAuthority(role.getName());
                })
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> {
                            System.out.println("Getting role: " + role.getName()); // Debug từng role
                            return new SimpleGrantedAuthority(role.getName());
                        })
                        .collect(Collectors.toSet())
        );
    }


}