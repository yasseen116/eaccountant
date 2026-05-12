package com.artal.eaccountant.stock;

// Types of stock movements supported by the inventory system.
public enum MovementType {
    ADDED,
    SALE,
    TRANSFER_TO_FULFILLMENT,
    RETURN_FROM_FULFILLMENT,
    ADJUSTMENT
}