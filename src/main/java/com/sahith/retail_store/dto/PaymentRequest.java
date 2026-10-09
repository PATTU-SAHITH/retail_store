package com.sahith.retail_store.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentRequest {
    private Long billId;
    private BigDecimal amount;

    public PaymentRequest() {}

    public PaymentRequest(Long billId, BigDecimal amount) {
        this.billId = billId;
        this.amount = amount;
    }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public static PaymentRequestBuilder builder() {
        return new PaymentRequestBuilder();
    }

    public static class PaymentRequestBuilder {
        private Long billId;
        private BigDecimal amount;

        public PaymentRequestBuilder billId(Long billId) {
            this.billId = billId;
            return this;
        }
        public PaymentRequestBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }
        public PaymentRequest build() {
            return new PaymentRequest(billId, amount);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRequest that = (PaymentRequest) o;
        return Objects.equals(billId, that.billId) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, amount);
    }
}
