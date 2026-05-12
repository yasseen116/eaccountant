package com.artal.eaccountant.inventory;

// Response row sent to React for the inventory table.
public record InventoryRow(
        Long itemId,
        String itemName,
        Long productCategoryId,
        String productCategoryName,
        String variation,
        int localStock,
        int fulfillmentStock,
        int localMinStock,
        int fulfillmentMinStock,
        int fulfillmentTargetStock,
        String restockStatus,
        int recommendedTransfer
) {
}