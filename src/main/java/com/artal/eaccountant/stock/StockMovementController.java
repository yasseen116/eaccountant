package com.artal.eaccountant.stock;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @GetMapping
    public List<MovementResponse> getMovements() {
        return stockMovementService.getMovements();
    }

    @GetMapping("/{id}")
    public MovementResponse getMovementById(@PathVariable Long id) {
        return stockMovementService.getMovementById(id);
    }

    @PostMapping
    public MovementResponse createMovement(@RequestBody MovementRequest request) {
        return stockMovementService.createMovement(request);
    }

    @PutMapping("/{id}")
    public MovementResponse updateMovement(@PathVariable Long id,
                                           @RequestBody MovementRequest request) {
        return stockMovementService.updateMovement(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMovement(@PathVariable Long id) {
        stockMovementService.deleteMovement(id);
    }
}