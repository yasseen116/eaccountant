package com.artal.eaccountant.catalog;
// Request received from React when creating or updating an item.
public record ItemRequest(
        String itemName,
        Long categoryId,
        String variation,
        int fbaMinStock,
        int localMinStock,
        int fbaTargetStock,
        boolean active
) {
}