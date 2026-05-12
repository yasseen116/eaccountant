package com.artal.eaccountant.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Handles database operations for items.
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Returns only active items.
    List<Item> findByActiveTrue();

    // Returns items by active status.
    List<Item> findByActive(boolean active);
}