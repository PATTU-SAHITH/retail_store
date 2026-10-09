package com.sahith.retail_store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    public enum OrderStatus {
        PENDING, RECEIVED, CANCELLED
    }

    public PurchaseOrder() {}

    public PurchaseOrder(Long id, Supplier supplier, LocalDateTime orderDate, BigDecimal totalAmount, OrderStatus status) {
        this.id = id;
        this.supplier = supplier;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public static PurchaseOrderBuilder builder() {
        return new PurchaseOrderBuilder();
    }

    public static class PurchaseOrderBuilder {
        private Long id;
        private Supplier supplier;
        private LocalDateTime orderDate;
        private BigDecimal totalAmount;
        private OrderStatus status;

        public PurchaseOrderBuilder id(Long id) { this.id = id; return this; }
        public PurchaseOrderBuilder supplier(Supplier supplier) { this.supplier = supplier; return this; }
        public PurchaseOrderBuilder orderDate(LocalDateTime orderDate) { this.orderDate = orderDate; return this; }
        public PurchaseOrderBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public PurchaseOrderBuilder status(OrderStatus status) { this.status = status; return this; }
        public PurchaseOrder build() { return new PurchaseOrder(id, supplier, orderDate, totalAmount, status); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
