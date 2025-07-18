package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.repository.*;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepo;

    public RoleServiceImpl(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepo.findByName(name);
    }
}