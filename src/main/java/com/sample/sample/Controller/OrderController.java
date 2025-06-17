package com.sample.sample.Controller;

import com.sample.sample.DTO.OrderDTO;
import com.sample.sample.Model.Orders;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerTypePredicate;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{accountId}/{addressId}")
    public AuthResponse placeOrder(@PathVariable Long accountId, @PathVariable Long addressId, @RequestBody OrderDTO request) {
        orderService.placeOrder(accountId,addressId,request);
        return new AuthResponse(HttpStatus.CREATED.value(), "success",null);
    }

    // GET all orders
    @GetMapping("/all")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET orders by account ID
    @GetMapping("/{accountId}")
    public ResponseEntity<List<Orders>> getOrdersByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(orderService.getOrdersByAccount(accountId));
    }



}
