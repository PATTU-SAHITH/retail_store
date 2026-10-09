package com.sahith.retail_store.config;

import com.sahith.retail_store.entity.Category;
import com.sahith.retail_store.entity.Product;
import com.sahith.retail_store.entity.Inventory;
import com.sahith.retail_store.entity.Customer;
import com.sahith.retail_store.repository.CategoryRepository;
import com.sahith.retail_store.repository.ProductRepository;
import com.sahith.retail_store.repository.InventoryRepository;
import com.sahith.retail_store.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            CustomerRepository customerRepository) {
        return args -> {
            // Check if data already exists to avoid Duplicate SKU errors
            if (productRepository.count() > 0) {
                return;
            }

            // 1. Create Categories
            Category electronics = Category.builder().name("Electronics").description("Gadgets and devices").build();
            Category grocery = Category.builder().name("Grocery").description("Daily essentials").build();
            categoryRepository.saveAll(List.of(electronics, grocery));

            // 2. Create Products
            Product laptop = Product.builder().name("Gaming Laptop").sku("LAP-001").price(new BigDecimal("1200.00")).category(electronics).build();
            Product mouse = Product.builder().name("Wireless Mouse").sku("MOU-001").price(new BigDecimal("25.00")).category(electronics).build();
            Product bread = Product.builder().name("Whole Wheat Bread").sku("BRD-001").price(new BigDecimal("3.50")).category(grocery).build();
            productRepository.saveAll(List.of(laptop, mouse, bread));

            // 3. Initialize Inventory
            inventoryRepository.save(Inventory.builder().product(laptop).quantity(10).minStockLevel(2).build());
            inventoryRepository.save(Inventory.builder().product(mouse).quantity(50).minStockLevel(5).build());
            inventoryRepository.save(Inventory.builder().product(bread).quantity(3).minStockLevel(10).build());

            // 4. Create Customers
            customerRepository.save(Customer.builder().name("John Doe").phone("1234567890").email("john@example.com").build());
            customerRepository.save(Customer.builder().name("Jane Smith").phone("9876543210").email("jane@example.com").build());
        };
    }
}
