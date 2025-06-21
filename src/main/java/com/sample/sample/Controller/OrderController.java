package com.sample.sample.Controller;

import com.sample.sample.DTO.OrderDTO;
import com.sample.sample.Model.Orders;
import com.sample.sample.Model.UserOrderedItems;
import com.sample.sample.Repository.UserOrderedItemRepository;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.OrdersResponse;
import com.sample.sample.Service.OrderService;
import com.sample.sample.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserOrderedItemRepository userOrderedItemRepository;

    // GET all orders
    @GetMapping("/all")
    public ResponseEntity<List<OrdersResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET orders by account ID
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<OrdersResponse>> getOrdersByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(orderService.getOrdersByAccountId(accountId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersResponse> getOrderByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderByOrderId(orderId));
    }


    //    User ordered Items
    @GetMapping("/account/{orderId}/items")
    public ResponseEntity<List<UserOrderedItems>> getOrderItems(@PathVariable Long orderId) {
        return ResponseEntity.ok(userOrderedItemRepository.findByOrderId(orderId));
    }

}
