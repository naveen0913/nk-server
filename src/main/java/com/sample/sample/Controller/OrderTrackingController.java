package com.example.demo.controller;

import com.example.demo.model.OrderTracking;
import com.example.demo.service.OrderTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
public class OrderTrackingController {

    @Autowired
    private OrderTrackingService orderTrackingService;

    @PostMapping("/{orderId}")
    public ResponseEntity<OrderTracking> createTracking(@PathVariable Long orderId, @RequestBody OrderTracking tracking) {
        return ResponseEntity.ok(orderTrackingService.saveTracking(orderId,tracking));
    }

    @GetMapping
    public ResponseEntity<List<OrderTracking>> getAllTrackings() {
        return ResponseEntity.ok(orderTrackingService.getAllTrackings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderTracking> getTrackingById(@PathVariable Long id) {
        return orderTrackingService.getTrackingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderTracking> updateTracking(@PathVariable Long id, @RequestBody OrderTracking tracking) {
        return ResponseEntity.ok(orderTrackingService.updateTracking(id, tracking));
    }



}
