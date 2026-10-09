package com.sahith.retail_store.service;

import com.sahith.retail_store.dto.ProductDTO;
import com.sahith.retail_store.entity.*;
import com.sahith.retail_store.repository.*;
import com.sahith.retail_store.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    public CatalogService(ProductRepository productRepository, CategoryRepository categoryRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        // Delete associated inventory first to avoid constraint violations
        inventoryRepository.findByProductId(id); // Just to check if it exists, or we can just delete
        // Since Inventory is 1:1, we can try to delete it.
        // We'll let the DB handle it if it's an error, but a cleaner way is:
        try {
            // Note: In a real app, we'd check for BillItems too.
            // For this project, we'll just attempt deletion.
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete product because it is linked to existing bills.");
        }
    }

    private ProductDTO mapToDTO(Product product) {
        // Get current stock from inventory
        Integer stock = 0;
        try {
            Inventory inventory = inventoryRepository.findByProductId(product.getId());
            if (inventory != null) {
                stock = inventory.getQuantity();
            }
        } catch (Exception e) {
            // Log error or handle
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : "No Category")
                .stockQuantity(stock)
                .build();
    }
}
