package com.sahith.retail_store.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class BillRequest {
    private Long customerId;
    private List<BillItemRequest> items;
    private BigDecimal amountPaid;

    public BillRequest() {}

    public BillRequest(Long customerId, List<BillItemRequest> items, BigDecimal amountPaid) {
        this.customerId = customerId;
        this.items = items;
        this.amountPaid = amountPaid;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public List<BillItemRequest> getItems() { return items; }
    public void setItems(List<BillItemRequest> items) { this.items = items; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public static BillRequestBuilder builder() {
        return new BillRequestBuilder();
    }

    public static class BillRequestBuilder {
        private Long customerId;
        private List<BillItemRequest> items;
        private BigDecimal amountPaid;

        public BillRequestBuilder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }
        public BillRequestBuilder items(List<BillItemRequest> items) {
            this.items = items;
            return this;
        }
        public BillRequestBuilder amountPaid(BigDecimal amountPaid) {
            this.amountPaid = amountPaid;
            return this;
        }
        public BillRequest build() {
            return new BillRequest(customerId, items, amountPaid);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillRequest that = (BillRequest) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(items, that.items) && Objects.equals(amountPaid, that.amountPaid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, items, amountPaid);
    }
}
