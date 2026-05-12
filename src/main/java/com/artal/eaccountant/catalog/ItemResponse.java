package com.artal.eaccountant.catalog;

// Response sent to React for the items table.
public record ItemResponse(
        Long id,
        String itemName,
        Long productCategoryId,
        String productCategoryName,
        String variation,
        int localMinStock,
        int fulfillmentMinStock,
        int fulfillmentTargetStock,
        boolean active
) {
}