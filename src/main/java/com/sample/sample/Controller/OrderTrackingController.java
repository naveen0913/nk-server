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
@RequestMapping("/api/tracking/orders")
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


//    @GetMapping
//    public ResponseEntity<List<OrderTracking>> getAllTrackings() {
//        return ResponseEntity.ok(orderTrackingService.getAllTrackings());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderTracking> getTrackingById(@PathVariable Long id) {
//        return orderTrackingService.getTrackingById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<OrderTracking> updateTracking(@PathVariable Long id, @RequestBody OrderTracking tracking) {
//        return ResponseEntity.ok(orderTrackingService.updateTracking(id, tracking));
//    }


}
