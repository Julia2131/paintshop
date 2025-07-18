package com.example.paintmanagement.repository;

import com.example.paintmanagement.entity.CartItem;
import com.example.paintmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
}