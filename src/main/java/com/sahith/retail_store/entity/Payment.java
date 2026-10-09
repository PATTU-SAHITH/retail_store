package com.sahith.retail_store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private BigDecimal amountPaid;

    public Payment() {}

    public Payment(Long id, Bill bill, LocalDateTime paymentDate, BigDecimal amountPaid) {
        this.id = id;
        this.bill = bill;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public static class PaymentBuilder {
        private Long id;
        private Bill bill;
        private LocalDateTime paymentDate;
        private BigDecimal amountPaid;

        public PaymentBuilder id(Long id) { this.id = id; return this; }
        public PaymentBuilder bill(Bill bill) { this.bill = bill; return this; }
        public PaymentBuilder paymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; return this; }
        public PaymentBuilder amountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; return this; }
        public Payment build() { return new Payment(id, bill, paymentDate, amountPaid); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
