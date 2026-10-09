package com.sahith.retail_store.dto;

import java.util.Objects;

public class BillItemRequest {
    private Long productId;
    private Integer quantity;

    public BillItemRequest() {}

    public BillItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public static BillItemRequestBuilder builder() {
        return new BillItemRequestBuilder();
    }

    public static class BillItemRequestBuilder {
        private Long productId;
        private Integer quantity;

        public BillItemRequestBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }
        public BillItemRequestBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }
        public BillItemRequest build() {
            return new BillItemRequest(productId, quantity);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItemRequest that = (BillItemRequest) o;
        return Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }
}
