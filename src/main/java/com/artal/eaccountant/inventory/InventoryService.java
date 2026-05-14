package com.artal.eaccountant.inventory;

import com.artal.eaccountant.catalog.Item;
import com.artal.eaccountant.catalog.ItemRepository;
import com.artal.eaccountant.inventory.specification.RestockStatusResolver;
import com.artal.eaccountant.stock.StockMovement;
import com.artal.eaccountant.stock.StockMovementRepository;
import com.artal.eaccountant.stock.strategy.StockEffect;
import com.artal.eaccountant.stock.strategy.StockMovementStrategy;
import com.artal.eaccountant.stock.strategy.StockMovementStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final ItemRepository itemRepository;
    private final StockMovementRepository movementRepository;
    private final StockMovementStrategyFactory strategyFactory;
    private final RestockStatusResolver restockStatusResolver;

    public InventoryService(ItemRepository itemRepository,
                            StockMovementRepository movementRepository,
                            StockMovementStrategyFactory strategyFactory,
                            RestockStatusResolver restockStatusResolver) {
        this.itemRepository = itemRepository;
        this.movementRepository = movementRepository;
        this.strategyFactory = strategyFactory;
        this.restockStatusResolver = restockStatusResolver;
    }

    public List<InventoryResponse> getInventory() {
        return itemRepository.findByActiveTrue()
                .stream()
                .map(this::buildInventoryResponse)
                .toList();
    }

    public List<InventoryResponse> getInventoryAlerts() {
        return getInventory()
                .stream()
                .filter(response -> response.restockStatus() != RestockStatus.OK)
                .toList();
    }

    public InventoryResponse getInventoryByItemId(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return buildInventoryResponse(item);
    }

    private InventoryResponse buildInventoryResponse(Item item) {
        InventorySnapshot snapshot = calculateSnapshot(item);

        RestockStatus restockStatus = restockStatusResolver.resolve(snapshot);

        int recommendedTransfer = calculateRecommendedTransferToFulfillment(snapshot);

        return new InventoryResponse(
                item.getId(),
                item.getItemName(),
                item.getProductCategory().getId(),
                item.getProductCategory().getName(),
                item.getVariation(),
                snapshot.localStock(),
                snapshot.fulfillmentStock(),
                item.getLocalMinStock(),
                item.getFulfillmentMinStock(),
                item.getFulfillmentTargetStock(),
                restockStatus,
                recommendedTransfer
        );
    }

    private InventorySnapshot calculateSnapshot(Item item) {
        List<StockMovement> movements = movementRepository.findByItemId(item.getId());

        int localStock = 0;
        int fulfillmentStock = 0;

        for (StockMovement movement : movements) {
            StockMovementStrategy strategy = strategyFactory.getStrategy(movement.getMovementType());
            StockEffect effect = strategy.calculateEffect(movement);

            localStock += effect.localChange();
            fulfillmentStock += effect.fulfillmentChange();
        }

        return new InventorySnapshot(item, localStock, fulfillmentStock);
    }

    private int calculateRecommendedTransferToFulfillment(InventorySnapshot snapshot) {
        int neededToReachTarget =
                snapshot.item().getFulfillmentTargetStock() - snapshot.fulfillmentStock();

        return Math.max(0, Math.min(snapshot.localStock(), neededToReachTarget));
    }
}
