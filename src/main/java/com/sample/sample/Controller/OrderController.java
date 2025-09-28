package com.sample.sample.Controller;

import com.razorpay.RazorpayException;
import com.sample.sample.DTO.OrderDTO;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.Orders;
import com.sample.sample.Model.UserOrderedItems;
import com.sample.sample.Repository.UserOrderedItemRepository;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.RazorpayResponse;
import com.sample.sample.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserOrderedItemRepository userOrderedItemRepository;

    @PostMapping("/create")
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO req) {
        try {
            Orders order = orderService.placeOrderFromCart(req);
            // If payment is ONLINE, controller should call createRazorpayOrder to get rzp details
            if ("ONLINE".equalsIgnoreCase(req.getPaymentMethod())) {
                RazorpayResponse r = orderService.createRazorpayOrder(order.getId());
                return ResponseEntity.status(HttpStatus.CREATED.value()).body(r);
            } else {
                // COD response: return order summary
                return ResponseEntity.status(HttpStatus.CREATED.value()).body("COD");
            }
        } catch (RazorpayException re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Razorpay error: " + re.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/verify/{orderId}")
    public ResponseEntity<?> verifyPayment(@PathVariable Long orderId,
                                           @RequestBody PaymentRequestDTO requestDTO) {
        try {
            orderService.verifyAndCapturePayment(orderId, requestDTO);
            return ResponseEntity.status(HttpStatus.OK.value()).body("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<AuthResponse> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
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

    @GetMapping("/account/{userId}")
    public ResponseEntity<?> getUserOrderItems(@PathVariable String userId) {
        AuthResponse items = orderService.getUsersOrdersList(userId);
        return ResponseEntity.status(items.getCode()).body(items);
    }


}
