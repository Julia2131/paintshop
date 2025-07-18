package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, List<CartItem> items);
    List<Order> findByUser(User user);
    List<Order> findAll(); // dành cho admin
}
