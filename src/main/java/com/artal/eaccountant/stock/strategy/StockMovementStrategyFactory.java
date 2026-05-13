package com.artal.eaccountant.stock.strategy;

import com.artal.eaccountant.stock.MovementType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StockMovementStrategyFactory {

    private final Map<MovementType, StockMovementStrategy> strategies;

    public StockMovementStrategyFactory(List<StockMovementStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        StockMovementStrategy::supports,
                        strategy -> strategy
                ));
    }

    public StockMovementStrategy getStrategy(MovementType movementType) {
        StockMovementStrategy strategy = strategies.get(movementType);

        if (strategy == null) {
            throw new RuntimeException("Unsupported movement type: " + movementType);
        }

        return strategy;
    }
}