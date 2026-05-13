package com.artal.eaccountant.stock.strategy;

import com.artal.eaccountant.stock.MovementRequest;
import com.artal.eaccountant.stock.MovementType;
import com.artal.eaccountant.stock.StockLocation;

public interface StockMovementStrategy {

    MovementType supports();

    StockLocation resolveLocation(MovementRequest request);

    StockEffect calculateEffect(int quantity);
}