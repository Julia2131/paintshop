package com.example.paintmanagement.controllers;

import com.example.paintmanagement.entity.Product;
import com.example.paintmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String viewProducts(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Chuyển hướng nếu chưa đăng nhập
        }
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products"; // Trả về products.html
    }
}
