package com.example.paintmanagement.controllers;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;

    public OrderController(UserService userService, CartService cartService, OrderService orderService) {
        this.userService = userService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<CartItem> cart = cartService.getCartByUser(user);
        orderService.createOrder(user, cart);
        cartService.clearCart(user);
        return "redirect:/orders";
    }

    @GetMapping
    public String viewOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Order> orders = orderService.findByUser(user);
        model.addAttribute("orders", orders);
        return "order-list";
    }
}