package com.artal.eaccountant.stock.strategy;

import com.artal.eaccountant.stock.MovementRequest;
import com.artal.eaccountant.stock.MovementType;
import com.artal.eaccountant.stock.StockLocation;
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
    public StockEffect calculateEffect(int quantity) {
        return new StockEffect(0, 0);
    }
}
