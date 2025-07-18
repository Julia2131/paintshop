package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public List<Product> findByCategoryId(Long id) {
        return productRepo.findByCategoryId(id);
    }

    public Product save(Product product) {
        return productRepo.save(product);
    }

    public Product findById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public List<Product> searchByTitle(String keyword) {
        return productRepo.findByTitleContainingIgnoreCase(keyword);
    }

    @Override
    public long count() {
        return productRepo.count();
    }
}
