package com.artal.eaccountant.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Handles database operations for stock movements.
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // Returns all movements for one item.
    List<StockMovement> findByItemId(Long itemId);
}