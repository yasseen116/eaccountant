package com.artal.eaccountant.stock.strategy;

public record StockEffect(
        int localChange,
        int fulfillmentChange
) {
}