package com.artal.eaccountant.inventory;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryResponse> getInventory() {
        return inventoryService.getInventory();
    }

    @GetMapping("/alerts")
    public List<InventoryResponse> getInventoryAlerts() {
        return inventoryService.getInventoryAlerts();
    }

    @GetMapping("/{itemId}")
    public InventoryResponse getInventoryByItemId(@PathVariable Long itemId) {
        return inventoryService.getInventoryByItemId(itemId);
    }
}
