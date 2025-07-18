package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;
import com.example.paintmanagement.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    public Category findById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public List<Category> searchByName(String keyword) {
        return categoryRepo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public long count() {
        return categoryRepo.count();
    }
}
