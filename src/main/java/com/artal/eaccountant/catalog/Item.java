package com.artal.eaccountant.catalog;

import jakarta.persistence.*;

@Entity
public class Item {

    // Auto-generated primary key for each item.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Main item name shown in the system.
    private String itemName;

    // Many items can belong to one category.
    @ManyToOne
    private ProductCategory productCategory;

    // Variation such as Silver, Black, or Design A.
    private String variation;

    // Minimum local stock before showing an alert.
    private int localMinStock;

    // Minimum fulfillment stock before showing an alert.
    private int fulfillmentMinStock;

    // Target fulfillment stock used for transfer recommendation.
    private int fulfillmentTargetStock;

    // Used to hide items without deleting old movement history.
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public String getVariation() {
        return variation;
    }

    public int getLocalMinStock() {
        return localMinStock;
    }

    public int getFulfillmentMinStock() {
        return fulfillmentMinStock;
    }

    public int getFulfillmentTargetStock() {
        return fulfillmentTargetStock;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public void setLocalMinStock(int localMinStock) {
        this.localMinStock = localMinStock;
    }

    public void setFulfillmentMinStock(int fulfillmentMinStock) {
        this.fulfillmentMinStock = fulfillmentMinStock;
    }

    public void setFulfillmentTargetStock(int fulfillmentTargetStock) {
        this.fulfillmentTargetStock = fulfillmentTargetStock;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}