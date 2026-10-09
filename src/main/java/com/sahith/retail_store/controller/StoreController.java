package com.sahith.retail_store.controller;

import com.sahith.retail_store.dto.*;
import com.sahith.retail_store.entity.Inventory;
import com.sahith.retail_store.service.BillingService;
import com.sahith.retail_store.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
public class StoreController {
    private final BillingService billingService;
    private final InventoryService inventoryService;

    public StoreController(BillingService billingService, InventoryService inventoryService) {
        this.billingService = billingService;
        this.inventoryService = inventoryService;
    }

    @PostMapping("/bill")
    public ResponseEntity<BillResponse> createBill(@RequestBody BillRequest request) {
        return ResponseEntity.ok(billingService.createBill(request));
    }

    @GetMapping("/inventory/low-stock")
    public ResponseEntity<List<Inventory>> getLowStock() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/inventory/{productId}")
    public ResponseEntity<Inventory> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStock(productId));
    }
}
