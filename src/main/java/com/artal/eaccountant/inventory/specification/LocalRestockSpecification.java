package com.artal.eaccountant.inventory.specification;

import com.artal.eaccountant.inventory.InventorySnapshot;
import com.artal.eaccountant.inventory.RestockStatus;
import org.springframework.stereotype.Component;

@Component
public class LocalRestockSpecification implements RestockSpecification {

    @Override
    public boolean isSatisfiedBy(InventorySnapshot snapshot) {
        return snapshot.localStock() < snapshot.item().getLocalMinStock();
    }

    @Override
    public RestockStatus getStatus() {
        return RestockStatus.LOCAL_RESTOCK;
    }
}
