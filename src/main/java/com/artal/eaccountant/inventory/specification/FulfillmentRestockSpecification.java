package com.artal.eaccountant.inventory.specification;

import com.artal.eaccountant.inventory.InventorySnapshot;
import com.artal.eaccountant.inventory.RestockStatus;
import org.springframework.stereotype.Component;

@Component
public class FulfillmentRestockSpecification implements RestockSpecification {

    @Override
    public boolean isSatisfiedBy(InventorySnapshot snapshot) {
        return snapshot.fulfillmentStock() < snapshot.item().getFulfillmentMinStock();
    }

    @Override
    public RestockStatus getStatus() {
        return RestockStatus.FULFILLMENT_RESTOCK;
    }
}
