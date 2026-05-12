package com.artal.eaccountant.catalog;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handles product category API requests.
@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryRepository categoryRepository;

    // Injects the category repository.
    public ProductCategoryController(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Returns active categories for dropdowns.
    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findByActiveTrue()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }
}