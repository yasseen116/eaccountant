package com.artal.eaccountant.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Handles database operations for product categories.
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    // Returns only active categories.
    List<ProductCategory> findByActiveTrue();
}