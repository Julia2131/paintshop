package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;

import java.util.List;

public interface CartService {
    List<CartItem> getCartByUser(User user);
    void addToCart(User user, Product product, int quantity);
    void removeItem(Long cartItemId);
    void clearCart(User user);
}
