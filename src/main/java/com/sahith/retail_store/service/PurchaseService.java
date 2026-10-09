package com.sahith.retail_store.service;

import com.sahith.retail_store.entity.*;
import com.sahith.retail_store.repository.*;
import com.sahith.retail_store.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final InventoryService inventoryService;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    public PurchaseService(PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderItemRepository purchaseOrderItemRepository,
                           InventoryService inventoryService, SupplierRepository supplierRepository,
                           ProductRepository productRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.inventoryService = inventoryService;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public PurchaseOrder createPurchase(Long supplierId, List<PurchaseItemRequest> items) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;

        PurchaseOrder order = PurchaseOrder.builder()
                .supplier(supplier)
                .orderDate(LocalDateTime.now())
                .status(PurchaseOrder.OrderStatus.RECEIVED)
                .build();

        order = purchaseOrderRepository.save(order);

        for (PurchaseItemRequest itemReq : items) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId()));

            // Update actual inventory - This now correctly uses the productId
            inventoryService.updateStock(product.getId(), itemReq.getQuantity());

            PurchaseOrderItem item = PurchaseOrderItem.builder()
                    .purchaseOrder(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .build();

            purchaseOrderItemRepository.save(item);
            totalAmount = totalAmount.add(itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }

        order.setTotalAmount(totalAmount);
        return purchaseOrderRepository.save(order);
    }

    public static class PurchaseItemRequest {
        private Long productId;
        private Integer quantity;
        private BigDecimal unitPrice;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }
}
