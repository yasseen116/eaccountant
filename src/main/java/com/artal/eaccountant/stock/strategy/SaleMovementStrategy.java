package com.artal.eaccountant.stock.strategy;

import com.artal.eaccountant.stock.MovementRequest;
import com.artal.eaccountant.stock.MovementType;
import com.artal.eaccountant.stock.StockLocation;
import org.springframework.stereotype.Component;

@Component
public class SaleMovementStrategy implements StockMovementStrategy {

    @Override
    public MovementType supports() {
        return MovementType.SALE;
    }

    @Override
    public StockLocation resolveLocation(MovementRequest request) {
        return StockLocation.FULFILLMENT;
    }

    @Override
    public StockEffect calculateEffect(int quantity) {
        return new StockEffect(0, -quantity);
    }
}
