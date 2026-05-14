package com.artal.eaccountant.stock.strategy;

import com.artal.eaccountant.stock.MovementRequest;
import com.artal.eaccountant.stock.MovementType;
import com.artal.eaccountant.stock.StockLocation;
import com.artal.eaccountant.stock.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class AdjustmentMovementStrategy implements StockMovementStrategy {

    @Override
    public MovementType supports() {
        return MovementType.ADJUSTMENT;
    }

    @Override
    public StockLocation resolveLocation(MovementRequest request) {
        return request.location();
    }

    @Override
    public StockEffect calculateEffect(StockMovement movement) {
        if (movement.getLocation() == StockLocation.LOCAL) {
            return new StockEffect(movement.getQuantity(), 0);
        }

        if (movement.getLocation() == StockLocation.FULFILLMENT) {
            return new StockEffect(0, movement.getQuantity());
        }

        return new StockEffect(0, 0);
    }
}
