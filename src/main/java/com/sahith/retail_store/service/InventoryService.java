package com.sahith.retail_store.service;

import com.sahith.retail_store.entity.*;
import com.sahith.retail_store.repository.*;
import com.sahith.retail_store.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public Inventory getStock(Long productId) {
        return Optional.ofNullable(inventoryRepository.findByProductId(productId))
                .orElseGet(() -> {
                    // Auto-initialize inventory if it doesn't exist
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

                    Inventory newInventory = Inventory.builder()
                            .product(product)
                            .quantity(0) // Start with 0
                            .minStockLevel(5) // Default min stock
                            .build();
                    return inventoryRepository.save(newInventory);
                });
    }

    @Transactional
    public void updateStock(Long productId, Integer quantityChange) {
        Inventory inventory = getStock(productId);
        int newQuantity = inventory.getQuantity() + quantityChange;

        if (newQuantity < 0) {
            throw new InsufficientStockException("Insufficient stock for product id: " + productId + ". Available: " + inventory.getQuantity());
        }

        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
    }

    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }
}
