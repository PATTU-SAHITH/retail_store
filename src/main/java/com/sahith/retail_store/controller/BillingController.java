package com.sahith.retail_store.controller;

import com.sahith.retail_store.dto.*;
import com.sahith.retail_store.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/bills")
    public ResponseEntity<BillResponse> createBill(@RequestBody BillRequest request) {
        return ResponseEntity.ok(billingService.createBill(request));
    }
}
