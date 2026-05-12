package com.artal.eaccountant.stock;

// Request received from React when creating or updating a stock movement.
public record MovementRequest(
        String movementDate,
        Long itemId,
        MovementType movementType,
        int quantity,
        StockLocation location,
        String notes
) {
}