package com.example.paintmanagement.service;

import com.example.paintmanagement.entity.*;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category save(Category category);
    Category findById(Long id);
    void deleteById(Long id);

    List<Category> searchByName(String keyword);
    long count();
}