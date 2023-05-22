package com.livecoding.blog.controller;

import com.livecoding.blog.entity.Category;
import com.livecoding.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RequestMapping("/api/categories")
@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable Long categoryId) {
        return categoryRepository.findById(categoryId).get();
    }

    @PostMapping("")
    public Category createCategory(@RequestBody Category body) {
        return categoryRepository.save(body);
    }

    @PutMapping("/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody Category body) {
        Category categoryToUpdate = categoryRepository.findById(categoryId).get();
        categoryToUpdate.setName(body.getName());
        return categoryRepository.save(categoryToUpdate);
    }

    @DeleteMapping("/{categoryId}")
    public Boolean deleteCategoryById(@PathVariable Long categoryId) {
        categoryRepository.deleteById(categoryId);
        return true;
    }
}
