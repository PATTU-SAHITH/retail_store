package com.sahith.retail_store.repository;

import com.sahith.retail_store.entity.CustomerLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerLedgerRepository extends JpaRepository<CustomerLedger, Long> {
    List<CustomerLedger> findByCustomerId(Long customerId);

    @Query("SELECT SUM(l.amount) FROM CustomerLedger l WHERE l.customer.id = :customerId")
    BigDecimal calculateBalanceByCustomerId(Long customerId);
}
