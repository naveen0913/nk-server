package com.sample.sample.Controller;

import com.sample.sample.DTO.DeliveryRequest;
import com.sample.sample.DTO.LocationDTO;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/admin/assign")
    public ResponseEntity<?> assignAgent(@RequestBody DeliveryRequest request) {
        return ResponseEntity.ok(deliveryService.assignDeliveryAgent(request));
    }

    @PostMapping("/update-location")
    public ResponseEntity<?> updateLocation(@RequestBody LocationDTO request) {
        return ResponseEntity.ok(deliveryService.updateLocation(
                request.getTrackingId(), request.getLatitude(), request.getLongitude(), request.getStatus()
        ));
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<?> getStatus(@PathVariable String orderId) {
        return ResponseEntity.ok(deliveryService.getDeliveryStatus(orderId));
    }

    @PostMapping("/agent/new")
    public ResponseEntity<?> addDeliveryAgent(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "status") boolean status

    ){
        AuthResponse authResponse = deliveryService.addNewDeliveryAgent(name,email,phone,status);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

    @GetMapping("/agent/all")
    public ResponseEntity<?> getDeliveryAgents(){
        AuthResponse response = deliveryService.getAllDeliveryAgents();
        return ResponseEntity.status(response.getCode()).body(response);
    }


}
