package com.sample.sample.Controller;

import com.sample.sample.DTO.OrderTrackingDTO;
import com.sample.sample.Model.OrdersTracking;
import com.sample.sample.Service.OrdersTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin("*")
public class OrderTrackingController {

    @Autowired
    private OrdersTrackingService orderTrackingService;

    @PostMapping("/{orderId}")
    public ResponseEntity<OrdersTracking> saveTracking(
            @PathVariable Long orderId,
            @RequestBody OrdersTracking trackingDto) {
        OrdersTracking saved = orderTrackingService.saveTracking(orderId, trackingDto);
        return new ResponseEntity(saved, HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<List<OrdersTracking>> getAllOrderTrackings() {
        return ResponseEntity.ok(orderTrackingService.getAllOrdersTracking());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrdersTracking> getTrackingByOrderId(@PathVariable Long orderId) {
        return orderTrackingService.getOrderTrackingById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<OrdersTracking> getTrackingById(@PathVariable Long trackingId) {
        return orderTrackingService.getTrackingById(trackingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrdersTracking> updateTracking(@PathVariable Long id, @RequestBody OrderTrackingDTO tracking) {
        return ResponseEntity.ok(orderTrackingService.updateTracking(id, tracking));
    }


}
