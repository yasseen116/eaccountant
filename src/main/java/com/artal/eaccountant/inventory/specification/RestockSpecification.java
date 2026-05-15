package com.artal.eaccountant.inventory.specification;

import com.artal.eaccountant.inventory.InventorySnapshot;
import com.artal.eaccountant.inventory.RestockStatus;

public interface RestockSpecification {

    boolean isSatisfiedBy(InventorySnapshot snapshot);

    RestockStatus getStatus();
}
