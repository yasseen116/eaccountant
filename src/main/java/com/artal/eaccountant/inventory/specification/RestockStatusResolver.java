package com.artal.eaccountant.inventory.specification;

import com.artal.eaccountant.inventory.InventorySnapshot;
import com.artal.eaccountant.inventory.RestockStatus;
import org.springframework.stereotype.Component;

@Component
public class RestockStatusResolver {

    private final RestockBothSpecification restockBothSpecification;
    private final FulfillmentRestockSpecification fulfillmentRestockSpecification;
    private final LocalRestockSpecification localRestockSpecification;
    private final OkStockSpecification okStockSpecification;

    public RestockStatusResolver(RestockBothSpecification restockBothSpecification,
                                 FulfillmentRestockSpecification fulfillmentRestockSpecification,
                                 LocalRestockSpecification localRestockSpecification,
                                 OkStockSpecification okStockSpecification) {
        this.restockBothSpecification = restockBothSpecification;
        this.fulfillmentRestockSpecification = fulfillmentRestockSpecification;
        this.localRestockSpecification = localRestockSpecification;
        this.okStockSpecification = okStockSpecification;
    }

    public RestockStatus resolve(InventorySnapshot snapshot) {
        if (restockBothSpecification.isSatisfiedBy(snapshot)) {
            return restockBothSpecification.getStatus();
        }

        if (fulfillmentRestockSpecification.isSatisfiedBy(snapshot)) {
            return fulfillmentRestockSpecification.getStatus();
        }

        if (localRestockSpecification.isSatisfiedBy(snapshot)) {
            return localRestockSpecification.getStatus();
        }

        return okStockSpecification.getStatus();
    }
}
