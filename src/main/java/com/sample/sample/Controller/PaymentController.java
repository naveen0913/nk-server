package com.sample.sample.Controller;

import com.razorpay.RazorpayException;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.Payment;
import com.sample.sample.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Create Razorpay Order
     */
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequestDTO requestDTO) {
        try {
            Payment payment = paymentService.createOrder(requestDTO.getAmount(), requestDTO.getCurrency(), requestDTO.getReceipt());
            return ResponseEntity.ok(payment);
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body("Failed to create order: " + e.getMessage());
        }
    }

    /**
     * Verify Razorpay Payment
     */
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentRequestDTO requestDTO) {
        boolean isVerified = paymentService.verifyPayment(requestDTO.getOrderId(), requestDTO.getPaymentId(), requestDTO.getSignature());

        if (isVerified) {
            return ResponseEntity.ok("Payment Verified Successfully");
        } else {
            return ResponseEntity.badRequest().body("Payment Verification Failed");
        }
    }
}

