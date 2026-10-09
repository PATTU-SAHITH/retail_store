package com.sahith.retail_store.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BillResponse {
    private Long billId;
    private String customerName;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal balanceDue;
    private String paymentStatus;
    private LocalDateTime billDate;
    private List<BillItemResponse> items;

    public BillResponse() {}

    public BillResponse(Long billId, String customerName, BigDecimal totalAmount, BigDecimal amountPaid, BigDecimal balanceDue, String paymentStatus, LocalDateTime billDate, List<BillItemResponse> items) {
        this.billId = billId;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.balanceDue = balanceDue;
        this.paymentStatus = paymentStatus;
        this.billDate = billDate;
        this.items = items;
    }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    public BigDecimal getBalanceDue() { return balanceDue; }
    public void setBalanceDue(BigDecimal balanceDue) { this.balanceDue = balanceDue; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getBillDate() { return billDate; }
    public void setBillDate(LocalDateTime billDate) { this.billDate = billDate; }
    public List<BillItemResponse> getItems() { return items; }
    public void setItems(List<BillItemResponse> items) { this.items = items; }

    public static BillResponseBuilder builder() {
        return new BillResponseBuilder();
    }

    public static class BillResponseBuilder {
        private Long billId;
        private String customerName;
        private BigDecimal totalAmount;
        private BigDecimal amountPaid;
        private BigDecimal balanceDue;
        private String paymentStatus;
        private LocalDateTime billDate;
        private List<BillItemResponse> items;

        public BillResponseBuilder billId(Long billId) {
            this.billId = billId;
            return this;
        }
        public BillResponseBuilder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }
        public BillResponseBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }
        public BillResponseBuilder amountPaid(BigDecimal amountPaid) {
            this.amountPaid = amountPaid;
            return this;
        }
        public BillResponseBuilder balanceDue(BigDecimal balanceDue) {
            this.balanceDue = balanceDue;
            return this;
        }
        public BillResponseBuilder paymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }
        public BillResponseBuilder billDate(LocalDateTime billDate) {
            this.billDate = billDate;
            return this;
        }
        public BillResponseBuilder items(List<BillItemResponse> items) {
            this.items = items;
            return this;
        }
        public BillResponse build() {
            return new BillResponse(billId, customerName, totalAmount, amountPaid, balanceDue, paymentStatus, billDate, items);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillResponse that = (BillResponse) o;
        return Objects.equals(billId, that.billId) && Objects.equals(customerName, that.customerName) && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(amountPaid, that.amountPaid) && Objects.equals(balanceDue, that.balanceDue) && Objects.equals(paymentStatus, that.paymentStatus) && Objects.equals(billDate, that.billDate) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, customerName, totalAmount, amountPaid, balanceDue, paymentStatus, billDate, items);
    }
}
