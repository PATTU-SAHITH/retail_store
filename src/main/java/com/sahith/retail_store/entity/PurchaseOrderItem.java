package com.sahith.retail_store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    public PurchaseOrderItem() {}

    public PurchaseOrderItem(Long id, PurchaseOrder purchaseOrder, Product product, Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.purchaseOrder = purchaseOrder;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public static PurchaseOrderItemBuilder builder() {
        return new PurchaseOrderItemBuilder();
    }

    public static class PurchaseOrderItemBuilder {
        private Long id;
        private PurchaseOrder purchaseOrder;
        private Product product;
        private Integer quantity;
        private BigDecimal unitPrice;

        public PurchaseOrderItemBuilder id(Long id) { this.id = id; return this; }
        public PurchaseOrderItemBuilder purchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; return this; }
        public PurchaseOrderItemBuilder product(Product product) { this.product = product; return this; }
        public PurchaseOrderItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public PurchaseOrderItemBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public PurchaseOrderItem build() { return new PurchaseOrderItem(id, purchaseOrder, product, quantity, unitPrice); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderItem that = (PurchaseOrderItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
