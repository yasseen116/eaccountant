package com.artal.eaccountant.inventory;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handles inventory API requests.
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // Injects inventory service.
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Returns calculated inventory for all active items.
    @GetMapping
    public List<InventoryRow> getInventory() {
        return inventoryService.getInventoryRows();
    }

    // Returns only inventory rows that need restocking.
    @GetMapping("/alerts")
    public List<InventoryRow> getInventoryAlerts() {
        return inventoryService.getInventoryAlerts();
    }

    // Returns calculated inventory for one item.
    @GetMapping("/{itemId}")
    public InventoryRow getInventoryForItem(@PathVariable Long itemId) {
        return inventoryService.getInventoryForItem(itemId);
    }
}