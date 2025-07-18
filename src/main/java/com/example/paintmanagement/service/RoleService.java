package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}