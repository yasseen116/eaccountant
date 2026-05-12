package com.artal.eaccountant.stock;

import com.artal.eaccountant.catalog.Item;
import com.artal.eaccountant.catalog.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Handles stock movement API requests.
@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementRepository movementRepository;
    private final ItemRepository itemRepository;

    // Injects repositories needed by this controller.
    public StockMovementController(StockMovementRepository movementRepository,
                                   ItemRepository itemRepository) {
        this.movementRepository = movementRepository;
        this.itemRepository = itemRepository;
    }

    // Returns all stock movements.
    @GetMapping
    public List<MovementResponse> getMovements() {
        return movementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Returns one stock movement by ID.
    @GetMapping("/{id}")
    public MovementResponse getMovementById(@PathVariable Long id) {
        StockMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock movement not found"));

        return toResponse(movement);
    }

    // Creates a new stock movement.
    @PostMapping
    public MovementResponse createMovement(@RequestBody MovementRequest request) {
        validateMovementRequest(request);

        Item item = findItem(request.itemId());

        StockMovement movement = new StockMovement();
        movement.setMovementDate(LocalDate.parse(request.movementDate()));
        movement.setItem(item);
        movement.setMovementType(request.movementType());
        movement.setQuantity(request.quantity());
        movement.setLocation(resolveLocation(request));
        movement.setNotes(request.notes());

        StockMovement savedMovement = movementRepository.save(movement);

        return toResponse(savedMovement);
    }

    // Updates an existing stock movement.
    @PutMapping("/{id}")
    public MovementResponse updateMovement(@PathVariable Long id,
                                           @RequestBody MovementRequest request) {
        validateMovementRequest(request);

        StockMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock movement not found"));

        Item item = findItem(request.itemId());

        movement.setMovementDate(LocalDate.parse(request.movementDate()));
        movement.setItem(item);
        movement.setMovementType(request.movementType());
        movement.setQuantity(request.quantity());
        movement.setLocation(resolveLocation(request));
        movement.setNotes(request.notes());

        StockMovement savedMovement = movementRepository.save(movement);

        return toResponse(savedMovement);
    }

    // Deletes a stock movement.
    @DeleteMapping("/{id}")
    public void deleteMovement(@PathVariable Long id) {
        if (!movementRepository.existsById(id)) {
            throw new RuntimeException("Stock movement not found");
        }

        movementRepository.deleteById(id);
    }

    // Finds an item by ID or throws an error.
    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // Validates stock movement request data.
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

    // Infers location for normal movements and keeps selected location for adjustments.
    private StockLocation resolveLocation(MovementRequest request) {
        if (request.movementType() == MovementType.ADJUSTMENT) {
            return request.location();
        }

        if (request.movementType() == MovementType.ADDED) {
            return StockLocation.LOCAL;
        }

        if (request.movementType() == MovementType.SALE) {
            return StockLocation.FULFILLMENT;
        }

        if (request.movementType() == MovementType.TRANSFER_TO_FULFILLMENT) {
            return StockLocation.FULFILLMENT;
        }

        if (request.movementType() == MovementType.RETURN_FROM_FULFILLMENT) {
            return StockLocation.LOCAL;
        }

        return null;
    }

    // Converts StockMovement entity to MovementResponse DTO.
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