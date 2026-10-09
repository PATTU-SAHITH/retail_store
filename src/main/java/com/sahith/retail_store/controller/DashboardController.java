package com.sahith.retail_store.controller;

import com.sahith.retail_store.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/most-sold")
    public ResponseEntity<List<DashboardService.ProductSales>> getMostSold() {
        return ResponseEntity.ok(dashboardService.getMostSoldProducts(5));
    }

    @GetMapping("/least-moving")
    public ResponseEntity<List<DashboardService.ProductSales>> getLeastMoving() {
        return ResponseEntity.ok(dashboardService.getLeastMovingProducts(5));
    }
}
