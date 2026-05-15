package com.artal.eaccountant.inventory.specification;

import com.artal.eaccountant.inventory.InventorySnapshot;
import com.artal.eaccountant.inventory.RestockStatus;
import org.springframework.stereotype.Component;

@Component
public class OkStockSpecification implements RestockSpecification {

    @Override
    public boolean isSatisfiedBy(InventorySnapshot snapshot) {
        return snapshot.localStock() >= snapshot.item().getLocalMinStock()
                && snapshot.fulfillmentStock() >= snapshot.item().getFulfillmentMinStock();
    }

    @Override
    public RestockStatus getStatus() {
        return RestockStatus.OK;
    }
}
