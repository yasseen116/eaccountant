package com.artal.eaccountant.inventory;

import com.artal.eaccountant.catalog.Item;
import com.artal.eaccountant.catalog.ItemRepository;
import com.artal.eaccountant.stock.MovementType;
import com.artal.eaccountant.stock.StockLocation;
import com.artal.eaccountant.stock.StockMovement;
import com.artal.eaccountant.stock.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Calculates inventory from stock movements.
@Service
public class InventoryService {

    private final ItemRepository itemRepository;
    private final StockMovementRepository movementRepository;

    // Injects repositories needed for inventory calculation.
    public InventoryService(ItemRepository itemRepository,
                            StockMovementRepository movementRepository) {
        this.itemRepository = itemRepository;
        this.movementRepository = movementRepository;
    }

    // Returns inventory rows for all items.
    public List<InventoryRow> getInventoryRows() {
        List<Item> items = itemRepository.findByActiveTrue();
        List<InventoryRow> rows = new ArrayList<>();

        for (Item item : items) {
            rows.add(calculateInventoryForItem(item));
        }

        return rows;
    }

    // Returns inventory row for one item.
    public InventoryRow getInventoryForItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return calculateInventoryForItem(item);
    }

    // Returns only rows that need restocking.
    public List<InventoryRow> getInventoryAlerts() {
        return getInventoryRows()
                .stream()
                .filter(row -> !row.restockStatus().equals("OK"))
                .toList();
    }

    // Calculates inventory numbers for one item.
    private InventoryRow calculateInventoryForItem(Item item) {
        List<StockMovement> movements = movementRepository.findByItemId(item.getId());

        int localStock = 0;
        int fulfillmentStock = 0;

        for (StockMovement movement : movements) {
            int quantity = movement.getQuantity();

            if (movement.getMovementType() == MovementType.ADDED) {
                localStock += quantity;
            }

            if (movement.getMovementType() == MovementType.SALE) {
                fulfillmentStock -= quantity;
            }

            if (movement.getMovementType() == MovementType.TRANSFER_TO_FULFILLMENT) {
                localStock -= quantity;
                fulfillmentStock += quantity;
            }

            if (movement.getMovementType() == MovementType.RETURN_FROM_FULFILLMENT) {
                localStock += quantity;
                fulfillmentStock -= quantity;
            }

            if (movement.getMovementType() == MovementType.ADJUSTMENT) {
                if (movement.getLocation() == StockLocation.LOCAL) {
                    localStock += quantity;
                }

                if (movement.getLocation() == StockLocation.FULFILLMENT) {
                    fulfillmentStock += quantity;
                }
            }
        }

        String restockStatus = calculateRestockStatus(item, localStock, fulfillmentStock);
        int recommendedTransfer = calculateRecommendedTransfer(item, localStock, fulfillmentStock);

        return new InventoryRow(
                item.getId(),
                item.getItemName(),
                item.getProductCategory().getId(),
                item.getProductCategory().getName(),
                item.getVariation(),
                localStock,
                fulfillmentStock,
                item.getLocalMinStock(),
                item.getFulfillmentMinStock(),
                item.getFulfillmentTargetStock(),
                restockStatus,
                recommendedTransfer
        );
    }

    // Calculates restock status based on minimum stock settings.
    private String calculateRestockStatus(Item item, int localStock, int fulfillmentStock) {
        boolean localLow = localStock < item.getLocalMinStock();
        boolean fulfillmentLow = fulfillmentStock < item.getFulfillmentMinStock();

        if (localLow && fulfillmentLow) {
            return "RESTOCK_BOTH";
        }

        if (fulfillmentLow) {
            return "FULFILLMENT_RESTOCK";
        }

        if (localLow) {
            return "LOCAL_RESTOCK";
        }

        return "OK";
    }

    // Calculates how much should be transferred to fulfillment.
    private int calculateRecommendedTransfer(Item item, int localStock, int fulfillmentStock) {
        int neededForTarget = item.getFulfillmentTargetStock() - fulfillmentStock;
        return Math.max(0, Math.min(localStock, neededForTarget));
    }
}