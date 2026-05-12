package com.artal.eaccountant.stock;

// Response sent to React for the stock movements table.
public record MovementResponse(
        Long id,
        String movementDate,
        Long itemId,
        String itemName,
        Long productCategoryId,
        String productCategoryName,
        MovementType movementType,
        int quantity,
        StockLocation location,
        String notes,
        String createdBy,
        String createdAt
) {
}