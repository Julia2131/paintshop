package com.example.paintmanagement.repository;

import com.example.paintmanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByTitleContainingIgnoreCase(String keyword);
}