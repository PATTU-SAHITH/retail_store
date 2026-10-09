package com.sahith.retail_store.service;

import com.sahith.retail_store.dto.*;
import com.sahith.retail_store.entity.*;
import com.sahith.retail_store.exception.*;
import com.sahith.retail_store.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final CustomerRepository customerRepository;
    private final CustomerLedgerRepository ledgerRepository;
    private final PaymentRepository paymentRepository;

    public BillingService(BillRepository billRepository, BillItemRepository billItemRepository,
                          ProductRepository productRepository, InventoryService inventoryService,
                          CustomerRepository customerRepository, CustomerLedgerRepository ledgerRepository,
                          PaymentRepository paymentRepository) {
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
        this.customerRepository = customerRepository;
        this.ledgerRepository = ledgerRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public BillResponse createBill(BillRequest request) {
        // 1. Validate Customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId()));

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<BillItem> billItems = new ArrayList<>();

        // 2. Process Items (Stock Validation & Total Calculation)
        for (BillItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.getProductId()));

            // Atomic stock decrement
            inventoryService.updateStock(product.getId(), -itemReq.getQuantity());

            BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(subTotal);

            billItems.add(BillItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(product.getPrice())
                    .subTotal(subTotal)
                    .build());
        }

        // 3. Determine Payment Status
        BigDecimal amountPaid = request.getAmountPaid() != null ? request.getAmountPaid() : BigDecimal.ZERO;
        Bill.PaymentStatus status = Bill.PaymentStatus.UNPAID;
        if (amountPaid.compareTo(totalAmount) >= 0) {
            status = Bill.PaymentStatus.PAID;
        } else if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            status = Bill.PaymentStatus.PARTIAL;
        }

        // 4. Create and Save Bill
        Bill bill = Bill.builder()
                .customer(customer)
                .billDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .paymentStatus(status)
                .build();

        bill = billRepository.save(bill);

        // 5. Save Bill Items
        for (BillItem item : billItems) {
            item.setBill(bill);
            billItemRepository.save(item);
        }

        // 6. Record Payment (if any)
        if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            Payment payment = Payment.builder()
                    .bill(bill)
                    .paymentDate(LocalDateTime.now())
                    .amountPaid(amountPaid)
                    .build();
            paymentRepository.save(payment);
        }

        // 7. Update Customer Ledger (Debt)
        BigDecimal balanceDue = totalAmount.subtract(amountPaid);
        if (balanceDue.compareTo(BigDecimal.ZERO) != 0) {
            CustomerLedger ledger = CustomerLedger.builder()
                    .customer(customer)
                    .transactionDate(LocalDateTime.now())
                    .amount(balanceDue)
                    .description("Bill #" + bill.getId() + " - Balance " + (balanceDue.compareTo(BigDecimal.ZERO) > 0 ? "Due" : "Credit"))
                    .bill(bill)
                    .build();
            ledgerRepository.save(ledger);
        }

        return mapToResponse(bill, amountPaid, balanceDue);
    }

    private BillResponse mapToResponse(Bill bill, BigDecimal amountPaid, BigDecimal balanceDue) {
        List<BillItem> items = billItemRepository.findByBillId(bill.getId());
        return BillResponse.builder()
                .billId(bill.getId())
                .customerName(bill.getCustomer().getName())
                .totalAmount(bill.getTotalAmount())
                .amountPaid(amountPaid)
                .balanceDue(balanceDue)
                .paymentStatus(bill.getPaymentStatus().name())
                .billDate(bill.getBillDate())
                .items(items.stream().map(i -> BillItemResponse.builder()
                        .productName(i.getProduct().getName())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .subTotal(i.getSubTotal())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
