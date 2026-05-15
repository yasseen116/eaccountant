package com.artal.eaccountant.dashboard;

import com.artal.eaccountant.inventory.InventoryResponse;
import com.artal.eaccountant.inventory.InventoryService;
import com.artal.eaccountant.inventory.RestockStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardFacade {

    private final InventoryService inventoryService;

    public DashboardFacade(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public DashboardSummaryResponse getSummary() {
        List<InventoryResponse> inventory = inventoryService.getInventory();

        long totalItems = inventory.size();

        int localStock = inventory.stream()
                .mapToInt(InventoryResponse::localStock)
                .sum();

        int fulfillmentStock = inventory.stream()
                .mapToInt(InventoryResponse::fulfillmentStock)
                .sum();

        long restockAlerts = inventory.stream()
                .filter(item -> item.restockStatus() != RestockStatus.OK)
                .count();

        int recommendedTransferToFulfillment = inventory.stream()
                .mapToInt(InventoryResponse::recommendedTransferToFulfillment)
                .sum();

        return new DashboardSummaryResponse(
                totalItems,
                localStock,
                fulfillmentStock,
                restockAlerts,
                recommendedTransferToFulfillment
        );
    }
}
