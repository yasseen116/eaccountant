package com.artal.eaccountant.catalog;

// Response sent to React for the items table.
public record ItemResponse(
        Long id,
        String itemName,
        Long productCategoryId,
        String productCategoryName,
        String variation,
        int fbaMinStock,
        int localMinStock,
        int fbaTargetStock,
        boolean active
) {
}