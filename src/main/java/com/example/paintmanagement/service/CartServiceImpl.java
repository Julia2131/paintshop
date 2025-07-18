package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepo;

    public CartServiceImpl(CartItemRepository repo) {
        this.cartItemRepo = repo;
    }

    public List<CartItem> getCartByUser(User user) {
        return cartItemRepo.findByUser(user);
    }

    @Override
    @Transactional
    public void addToCart(User user, Product product, int quantity) {
        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);
        cartItemRepo.save(item);
    }

    @Override
    @Transactional
    public void removeItem(Long id) {
        cartItemRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        cartItemRepo.deleteByUser(user);
    }
}
