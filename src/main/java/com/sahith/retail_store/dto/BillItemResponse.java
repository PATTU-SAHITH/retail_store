package com.sahith.retail_store.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class BillItemResponse {
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;

    public BillItemResponse() {}

    public BillItemResponse(String productName, Integer quantity, BigDecimal unitPrice, BigDecimal subTotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getSubTotal() { return subTotal; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }

    public static BillItemResponseBuilder builder() {
        return new BillItemResponseBuilder();
    }

    public static class BillItemResponseBuilder {
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;

        public BillItemResponseBuilder productName(String productName) {
            this.productName = productName;
            return this;
        }
        public BillItemResponseBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }
        public BillItemResponseBuilder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }
        public BillItemResponseBuilder subTotal(BigDecimal subTotal) {
            this.subTotal = subTotal;
            return this;
        }
        public BillItemResponse build() {
            return new BillItemResponse(productName, quantity, unitPrice, subTotal);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItemResponse that = (BillItemResponse) o;
        return Objects.equals(productName, that.productName) && Objects.equals(quantity, that.quantity) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(subTotal, that.subTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, quantity, unitPrice, subTotal);
    }
}
