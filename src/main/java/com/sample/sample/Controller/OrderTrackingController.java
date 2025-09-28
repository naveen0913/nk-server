package com.sample.sample.Controller;

import com.sample.sample.DTO.OrderTrackingDTO;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.OrdersTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
public class OrderTrackingController {

    @Autowired
    private OrdersTrackingService orderTrackingService;

    // Get all order tracking entries
    @GetMapping("/all")
    public ResponseEntity<AuthResponse> getAllOrderTrackings() {
        return ResponseEntity.ok(orderTrackingService.getAllOrdersTracking());
    }

    // Get tracking by order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<AuthResponse> getTrackingByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderTrackingService.getOrderTrackingById(orderId));
    }

    // Get tracking by tracking ID
    @GetMapping("/{trackingId}")
    public ResponseEntity<AuthResponse> getTrackingById(@PathVariable Long trackingId) {
        return ResponseEntity.ok(orderTrackingService.getTrackingById(trackingId));
    }

    // Update tracking by ID
    @PutMapping("/order/{id}")
    public ResponseEntity<AuthResponse> updateTracking(@PathVariable Long id, @RequestBody OrderTrackingDTO tracking) {
        return ResponseEntity.ok(orderTrackingService.updateTracking(id, tracking));
    }

}
