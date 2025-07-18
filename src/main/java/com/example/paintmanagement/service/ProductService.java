package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    List<Product> findByCategoryId(Long categoryId);
    Product save(Product product);
    Product findById(Long id);
    void deleteById(Long id);

    List<Product> searchByTitle(String keyword);

    long count();
}
