package com.sample.sample.Controller;

import com.sample.sample.Model.UserOrderedItems;
import com.sample.sample.Repository.UserOrderedItemRepository;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserOrderedItemRepository userOrderedItemRepository;

    @GetMapping("/all")
    public ResponseEntity<AuthResponse> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    @GetMapping("/account/{accountId}")
    public ResponseEntity<AuthResponse> getOrdersByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(orderService.getOrdersByAccountId(accountId));
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<AuthResponse> getOrderByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderByOrderId(orderId));
    }


    @GetMapping("/account/{orderId}/items")
    public ResponseEntity<AuthResponse> getOrderItems(@PathVariable Long orderId) {
        List<UserOrderedItems> items = userOrderedItemRepository.findByOrderId(orderId);
        return ResponseEntity.ok(new AuthResponse(HttpStatus.OK.value(), "Order items fetched successfully", items));
    }


}
