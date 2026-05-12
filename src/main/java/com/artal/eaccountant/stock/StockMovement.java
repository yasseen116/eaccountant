package com.artal.eaccountant.stock;

import com.artal.eaccountant.catalog.Item;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class StockMovement {

    // Auto-generated primary key for each stock movement.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The business date of the stock movement.
    private LocalDate movementDate;

    // The item affected by this movement.
    @ManyToOne(optional = false)
    private Item item;

    // The type of stock movement.
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    // Quantity moved, sold, added, or adjusted.
    private int quantity;

    // Location is mainly used for manual adjustments.
    @Enumerated(EnumType.STRING)
    private StockLocation location;

    // Optional note written by the admin.
    private String notes;

    // Temporary creator name until login is added.
    private String createdBy = "Admin";

    // Timestamp when the movement was created.
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public LocalDate getMovementDate() {
        return movementDate;
    }

    public Item getItem() {
        return item;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public StockLocation getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMovementDate(LocalDate movementDate) {
        this.movementDate = movementDate;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLocation(StockLocation location) {
        this.location = location;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}