package com.sahith.retail_store.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class LedgerEntryDTO {
    private LocalDateTime date;
    private BigDecimal amount;
    private String description;

    public LedgerEntryDTO() {}

    public LedgerEntryDTO(LocalDateTime date, BigDecimal amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static LedgerEntryDTOBuilder builder() {
        return new LedgerEntryDTOBuilder();
    }

    public static class LedgerEntryDTOBuilder {
        private LocalDateTime date;
        private BigDecimal amount;
        private String description;

        public LedgerEntryDTOBuilder date(LocalDateTime date) {
            this.date = date;
            return this;
        }
        public LedgerEntryDTOBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }
        public LedgerEntryDTOBuilder description(String description) {
            this.description = description;
            return this;
        }
        public LedgerEntryDTO build() {
            return new LedgerEntryDTO(date, amount, description);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerEntryDTO that = (LedgerEntryDTO) o;
        return Objects.equals(date, that.date) && Objects.equals(amount, that.amount) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, description);
    }
}
