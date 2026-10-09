package com.sahith.retail_store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "customer_ledgers")
public class CustomerLedger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill;

    public CustomerLedger() {}

    public CustomerLedger(Long id, Customer customer, LocalDateTime transactionDate, BigDecimal amount, String description, Bill bill) {
        this.id = id;
        this.customer = customer;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;
        this.bill = bill;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }

    public static CustomerLedgerBuilder builder() {
        return new CustomerLedgerBuilder();
    }

    public static class CustomerLedgerBuilder {
        private Long id;
        private Customer customer;
        private LocalDateTime transactionDate;
        private BigDecimal amount;
        private String description;
        private Bill bill;

        public CustomerLedgerBuilder id(Long id) { this.id = id; return this; }
        public CustomerLedgerBuilder customer(Customer customer) { this.customer = customer; return this; }
        public CustomerLedgerBuilder transactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; return this; }
        public CustomerLedgerBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public CustomerLedgerBuilder description(String description) { this.description = description; return this; }
        public CustomerLedgerBuilder bill(Bill bill) { this.bill = bill; return this; }
        public CustomerLedger build() { return new CustomerLedger(id, customer, transactionDate, amount, description, bill); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerLedger that = (CustomerLedger) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
