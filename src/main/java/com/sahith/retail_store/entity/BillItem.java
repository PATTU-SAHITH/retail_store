package com.sahith.retail_store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bill_items")
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal subTotal;

    public BillItem() {}

    public BillItem(Long id, Bill bill, Product product, Integer quantity, BigDecimal unitPrice, BigDecimal subTotal) {
        this.id = id;
        this.bill = bill;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getSubTotal() { return subTotal; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }

    public static BillItemBuilder builder() {
        return new BillItemBuilder();
    }

    public static class BillItemBuilder {
        private Long id;
        private Bill bill;
        private Product product;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;

        public BillItemBuilder id(Long id) { this.id = id; return this; }
        public BillItemBuilder bill(Bill bill) { this.bill = bill; return this; }
        public BillItemBuilder product(Product product) { this.product = product; return this; }
        public BillItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public BillItemBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public BillItemBuilder subTotal(BigDecimal subTotal) { this.subTotal = subTotal; return this; }
        public BillItem build() { return new BillItem(id, bill, product, quantity, unitPrice, subTotal); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItem billItem = (BillItem) o;
        return Objects.equals(id, billItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
