package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public OrderServiceImpl(OrderRepository or, OrderItemRepository oir) {
        this.orderRepo = or;
        this.orderItemRepo = oir;
    }

    public Order createOrder(User user, List<CartItem> cartItems) {
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = cartItems.stream().map(ci -> {
            OrderItem item = new OrderItem();
            item.setProduct(ci.getProduct());
            item.setQuantity(ci.getQuantity());
            item.setOrder(order);
            return item;
        }).toList();

        order.setItems(items);
        return orderRepo.save(order);
    }

    public List<Order> findByUser(User user) {
        return orderRepo.findByUser(user);
    }

    public List<Order> findAll() {
        return orderRepo.findAll();
    }
}
