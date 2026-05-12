package com.artal.eaccountant.catalog;

import jakarta.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ItemName;

    @ManyToOne
    private ProductCategory ProductCategory;


    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public ProductCategory getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        ProductCategory = productCategory;
    }

    public int getFbaMinStock() {
        return fbaMinStock;
    }

    public void setFbaMinStock(int fbaMinStock) {
        this.fbaMinStock = fbaMinStock;
    }

    public int getLocalMinStock() {
        return localMinStock;
    }

    public void setLocalMinStock(int localMinStock) {
        this.localMinStock = localMinStock;
    }

    public int getFbaTargetStock() {
        return fbaTargetStock;
    }

    public void setFbaTargetStock(int fbaTargetStock) {
        this.fbaTargetStock = fbaTargetStock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Variation such as Silver, Black, or Design A.
    private String variation;

    // Minimum FBA stock before showing an alert.
    private int fbaMinStock;

    // Minimum local stock before showing an alert.
    private int localMinStock;

    // Target FBA stock used for send-to-FBA recommendation.
    private int fbaTargetStock;

    // Used to hide items without deleting old movement history.
    private boolean active = true;
}
