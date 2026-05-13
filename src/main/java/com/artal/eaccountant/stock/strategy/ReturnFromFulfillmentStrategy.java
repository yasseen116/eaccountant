package com.artal.eaccountant.stock.strategy;

import com.artal.eaccountant.stock.MovementRequest;
import com.artal.eaccountant.stock.MovementType;
import com.artal.eaccountant.stock.StockLocation;
import org.springframework.stereotype.Component;

@Component
public class ReturnFromFulfillmentStrategy implements StockMovementStrategy {

    @Override
    public MovementType supports() {
        return MovementType.RETURN_FROM_FULFILLMENT;
    }

    @Override
    public StockLocation resolveLocation(MovementRequest request) {
        return StockLocation.LOCAL;
    }

    @Override
    public StockEffect calculateEffect(int quantity) {
        return new StockEffect(quantity, -quantity);
    }
}
