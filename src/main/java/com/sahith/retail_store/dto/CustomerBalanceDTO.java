package com.sahith.retail_store.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class CustomerBalanceDTO {
    private String customerName;
    private BigDecimal totalBalance;

    public CustomerBalanceDTO() {}

    public CustomerBalanceDTO(String customerName, BigDecimal totalBalance) {
        this.customerName = customerName;
        this.totalBalance = totalBalance;
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public BigDecimal getTotalBalance() { return totalBalance; }
    public void setTotalBalance(BigDecimal totalBalance) { this.totalBalance = totalBalance; }

    public static CustomerBalanceDTOBuilder builder() {
        return new CustomerBalanceDTOBuilder();
    }

    public static class CustomerBalanceDTOBuilder {
        private String customerName;
        private BigDecimal totalBalance;

        public CustomerBalanceDTOBuilder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }
        public CustomerBalanceDTOBuilder totalBalance(BigDecimal totalBalance) {
            this.totalBalance = totalBalance;
            return this;
        }
        public CustomerBalanceDTO build() {
            return new CustomerBalanceDTO(customerName, totalBalance);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerBalanceDTO that = (CustomerBalanceDTO) o;
        return Objects.equals(customerName, that.customerName) && Objects.equals(totalBalance, that.totalBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName, totalBalance);
    }
}
