package com.sahith.retail_store.service;

import com.sahith.retail_store.entity.*;
import com.sahith.retail_store.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final InventoryService inventoryService;
    private final CustomerLedgerRepository ledgerRepository;

    public DashboardService(BillRepository billRepository, BillItemRepository billItemRepository,
                            InventoryService inventoryService, CustomerLedgerRepository ledgerRepository) {
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.inventoryService = inventoryService;
        this.ledgerRepository = ledgerRepository;
    }

    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Today's Revenue
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        BigDecimal todayRevenue = billRepository.findAll().stream()
                .filter(b -> b.getBillDate().isAfter(startOfDay))
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.put("todayRevenue", todayRevenue);
        summary.put("totalBills", billRepository.count());
        summary.put("lowStockCount", inventoryService.getLowStockItems().size());

        // Total Outstanding Debt
        BigDecimal totalDebt = ledgerRepository.findAll().stream()
                .map(CustomerLedger::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.put("totalDebt", totalDebt);

        return summary;
    }

    public List<ProductSales> getMostSoldProducts(int limit) {
        // This is a simplification. In a real app, we'd use a @Query.
        Map<Long, Integer> salesCount = new HashMap<>();
        billItemRepository.findAll().forEach(item -> {
            salesCount.merge(item.getProduct().getId(), item.getQuantity(), Integer::sum);
        });

        return salesCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> new ProductSales(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<ProductSales> getLeastMovingProducts(int limit) {
        Map<Long, Integer> salesCount = new HashMap<>();
        billItemRepository.findAll().forEach(item -> {
            salesCount.merge(item.getProduct().getId(), item.getQuantity(), Integer::sum);
        });

        return salesCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue())
                .limit(limit)
                .map(entry -> new ProductSales(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public static class ProductSales {
        public Long productId;
        public Integer quantity;

        public ProductSales(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
