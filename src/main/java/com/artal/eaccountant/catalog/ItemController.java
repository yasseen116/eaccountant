package com.artal.eaccountant.catalog;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final ProductCategoryRepository categoryRepository;

    // Injects repositories needed by this controller.
    public ItemController(ItemRepository itemRepository,
                          ProductCategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    // Returns items, optionally filtered by active status.
    @GetMapping
    public List<ItemResponse> getItems(@RequestParam(required = false) Boolean active) {
        List<Item> items;

        if (active == null) {
            items = itemRepository.findAll();
        } else {
            items = itemRepository.findByActive(active);
        }

        return items.stream()
                .map(this::toResponse)
                .toList();
    }

    // Returns one item by ID.
    @GetMapping("/{id}")
    public ItemResponse getItemById(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return toResponse(item);
    }

    // Creates a new item.
    @PostMapping
    public ItemResponse createItem(@RequestBody ItemRequest request) {
        validateItemRequest(request);

        ProductCategory category = findCategory(request.categoryId());

        Item item = new Item();
        item.setItemName(request.itemName());
        item.setProductCategory(category);
        item.setVariation(request.variation());
        item.setLocalMinStock(request.localMinStock());
        item.setFulfillmentMinStock(request.fulfillmentMinStock());
        item.setFulfillmentTargetStock(request.fulfillmentTargetStock());
        item.setActive(request.active());

        Item savedItem = itemRepository.save(item);

        return toResponse(savedItem);
    }

    // Updates an existing item.
    @PutMapping("/{id}")
    public ItemResponse updateItem(@PathVariable Long id,
                                   @RequestBody ItemRequest request) {
        validateItemRequest(request);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        ProductCategory category = findCategory(request.categoryId());

        item.setItemName(request.itemName());
        item.setProductCategory(category);
        item.setVariation(request.variation());
        item.setLocalMinStock(request.localMinStock());
        item.setFulfillmentMinStock(request.fulfillmentMinStock());
        item.setFulfillmentTargetStock(request.fulfillmentTargetStock());
        item.setActive(request.active());

        Item savedItem = itemRepository.save(item);

        return toResponse(savedItem);
    }

    // Activates an item.
    @PatchMapping("/{id}/activate")
    public ItemResponse activateItem(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setActive(true);

        return toResponse(itemRepository.save(item));
    }

    // Deactivates an item without deleting old data.
    @PatchMapping("/{id}/deactivate")
    public ItemResponse deactivateItem(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setActive(false);

        return toResponse(itemRepository.save(item));
    }

    // Finds a category by ID or throws an error.
    private ProductCategory findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Validates item request data.
    private void validateItemRequest(ItemRequest request) {
        if (request.itemName() == null || request.itemName().isBlank()) {
            throw new RuntimeException("Item name is required");
        }

        if (request.categoryId() == null) {
            throw new RuntimeException("Product category is required");
        }

        if (request.localMinStock() < 0 ||
                request.fulfillmentMinStock() < 0 ||
                request.fulfillmentTargetStock() < 0) {
            throw new RuntimeException("Stock thresholds must be zero or positive");
        }

        if (request.fulfillmentTargetStock() < request.fulfillmentMinStock()) {
            throw new RuntimeException("Fulfillment target stock cannot be less than fulfillment minimum stock");
        }
    }

    // Converts Item entity to ItemResponse DTO.
    private ItemResponse toResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getItemName(),
                item.getProductCategory().getId(),
                item.getProductCategory().getName(),
                item.getVariation(),
                item.getLocalMinStock(),
                item.getFulfillmentMinStock(),
                item.getFulfillmentTargetStock(),
                item.isActive()
        );
    }
}