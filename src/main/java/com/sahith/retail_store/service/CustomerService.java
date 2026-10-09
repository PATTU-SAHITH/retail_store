package com.sahith.retail_store.service;

import com.sahith.retail_store.dto.*;
import com.sahith.retail_store.entity.*;
import com.sahith.retail_store.repository.*;
import com.sahith.retail_store.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerLedgerRepository ledgerRepository;
    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerLedgerRepository ledgerRepository,
                          PaymentRepository paymentRepository, BillRepository billRepository) {
        this.customerRepository = customerRepository;
        this.ledgerRepository = ledgerRepository;
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer details) {
        Customer customer = getCustomer(id);
        customer.setName(details.getName());
        customer.setPhone(details.getPhone());
        customer.setEmail(details.getEmail());
        customer.setAddress(details.getAddress());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public CustomerBalanceDTO getBalance(Long customerId) {
        Customer customer = getCustomer(customerId);
        BigDecimal balance = ledgerRepository.calculateBalanceByCustomerId(customerId);
        if (balance == null) balance = BigDecimal.ZERO;

        return CustomerBalanceDTO.builder()
                .customerName(customer.getName())
                .totalBalance(balance)
                .build();
    }

    @Transactional
    public void processPayment(PaymentRequest request) {
        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        // 1. Record the payment
        Payment payment = Payment.builder()
                .bill(bill)
                .paymentDate(LocalDateTime.now())
                .amountPaid(request.getAmount())
                .build();
        paymentRepository.save(payment);

        // 2. Update the Ledger (Negative amount reduces debt)
        CustomerLedger ledger = CustomerLedger.builder()
                .customer(bill.getCustomer())
                .transactionDate(LocalDateTime.now())
                .amount(request.getAmount().negate())
                .description("Payment against Bill #" + bill.getId())
                .bill(bill)
                .build();
        ledgerRepository.save(ledger);

        // 3. Update Bill Status
        BigDecimal currentPaid = paymentRepository.findAll().stream()
                .filter(p -> p.getBill().getId().equals(bill.getId()))
                .map(Payment::getAmountPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (currentPaid.compareTo(bill.getTotalAmount()) >= 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PAID);
        } else if (currentPaid.compareTo(BigDecimal.ZERO) > 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PARTIAL);
        }
        billRepository.save(bill);
    }

    public List<LedgerEntryDTO> getStatement(Long customerId) {
        return ledgerRepository.findByCustomerId(customerId).stream()
                .map(l -> LedgerEntryDTO.builder()
                        .date(l.getTransactionDate())
                        .amount(l.getAmount())
                        .description(l.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
