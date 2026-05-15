package com.artal.eaccountant.inventory;

public record InventoryResponse(
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
        RestockStatus restockStatus,
        int recommendedTransferToFulfillment
) {
}
