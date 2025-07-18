package com.example.paintmanagement.controllers;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public AdminController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("categoriesCount", categoryService.count());
        model.addAttribute("productsCount",   productService.count());
        return "admin/dashboard";
    }

    // ==== CATEGORY CRUD ====
    @GetMapping("/categories")
    public String viewCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/category-list";
    }

    @GetMapping("/categories/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }

    // ==== UPDATE ====
    @GetMapping("/categories/edit/{id}")
    public String editCategoryForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            // nếu không tìm thấy, redirect về list
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category);
        return "admin/category-form";
    }

    // ==== SEARCH ====
    @GetMapping("/categories/search")
    public String searchCategories(@RequestParam("keyword") String keyword, Model model) {
        List<Category> results = categoryService.searchByName(keyword);
        model.addAttribute("categories", results);
        model.addAttribute("keyword", keyword);
        return "admin/category-list";
    }

    // ==== PRODUCT CRUD ====
//    @GetMapping("/products")
//    public String productManagement(Model model) {
//        List<Product> products = productService.findAll();
//        model.addAttribute("products", products);
//        return "admin/products"; // templates/admin/products.html
//    }
    @GetMapping("/products")
    public String viewProducts(@RequestParam(value = "keyword", required = false) String keyword,
                               Model model) {
        List<Product> products;
        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchByTitle(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            products = productService.findAll();
        }
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }
}