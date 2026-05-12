package com.artal.eaccountant.catalog;

// Request received from React when creating or updating an item.
public record ItemRequest(
        String itemName,
        Long categoryId,
        String variation,
        int localMinStock,
        int fulfillmentMinStock,
        int fulfillmentTargetStock,
        boolean active
) {
}