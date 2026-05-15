package com.artal.eaccountant.inventory;

import com.artal.eaccountant.catalog.Item;

public record InventorySnapshot(
        Item item,
        int localStock,
        int fulfillmentStock
) {
}
