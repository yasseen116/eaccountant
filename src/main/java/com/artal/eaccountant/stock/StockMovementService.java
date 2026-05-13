package com.artal.eaccountant.stock;

import com.artal.eaccountant.catalog.Item;
import com.artal.eaccountant.catalog.ItemRepository;
import com.artal.eaccountant.stock.strategy.StockMovementStrategy;
import com.artal.eaccountant.stock.strategy.StockMovementStrategyFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockMovementService {

    private final StockMovementRepository movementRepository;
    private final ItemRepository itemRepository;
    private final StockMovementStrategyFactory strategyFactory;

    public StockMovementService(StockMovementRepository movementRepository,
                                ItemRepository itemRepository,
                                StockMovementStrategyFactory strategyFactory) {
        this.movementRepository = movementRepository;
        this.itemRepository = itemRepository;
        this.strategyFactory = strategyFactory;
    }

    public List<MovementResponse> getMovements() {
        return movementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MovementResponse getMovementById(Long id) {
        StockMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock movement not found"));

        return toResponse(movement);
    }

    public MovementResponse createMovement(MovementRequest request) {
        validateMovementRequest(request);

        Item item = findItem(request.itemId());

        StockMovementStrategy strategy = strategyFactory.getStrategy(request.movementType());

        StockMovement movement = new StockMovement();
        movement.setMovementDate(LocalDate.parse(request.movementDate()));
        movement.setItem(item);
        movement.setMovementType(request.movementType());
        movement.setQuantity(request.quantity());
        movement.setLocation(strategy.resolveLocation(request));
        movement.setNotes(request.notes());

        StockMovement savedMovement = movementRepository.save(movement);

        return toResponse(savedMovement);
    }

    public MovementResponse updateMovement(Long id, MovementRequest request) {
        validateMovementRequest(request);

        StockMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock movement not found"));

        Item item = findItem(request.itemId());

        StockMovementStrategy strategy = strategyFactory.getStrategy(request.movementType());

        movement.setMovementDate(LocalDate.parse(request.movementDate()));
        movement.setItem(item);
        movement.setMovementType(request.movementType());
        movement.setQuantity(request.quantity());
        movement.setLocation(strategy.resolveLocation(request));
        movement.setNotes(request.notes());

        StockMovement savedMovement = movementRepository.save(movement);

        return toResponse(savedMovement);
    }

    public void deleteMovement(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new RuntimeException("Stock movement not found");
        }

        movementRepository.deleteById(id);
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    private void validateMovementRequest(MovementRequest request) {
        if (request.movementDate() == null || request.movementDate().isBlank()) {
            throw new RuntimeException("Movement date is required");
        }

        if (request.itemId() == null) {
            throw new RuntimeException("Item is required");
        }

        if (request.movementType() == null) {
            throw new RuntimeException("Movement type is required");
        }

        if (request.quantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        if (request.movementType() == MovementType.ADJUSTMENT && request.location() == null) {
            throw new RuntimeException("Location is required for adjustment movements");
        }
    }

    private MovementResponse toResponse(StockMovement movement) {
        return new MovementResponse(
                movement.getId(),
                movement.getMovementDate().toString(),
                movement.getItem().getId(),
                movement.getItem().getItemName(),
                movement.getItem().getProductCategory().getId(),
                movement.getItem().getProductCategory().getName(),
                movement.getMovementType(),
                movement.getQuantity(),
                movement.getLocation(),
                movement.getNotes(),
                movement.getCreatedBy(),
                movement.getCreatedAt().toString()
        );
    }
}