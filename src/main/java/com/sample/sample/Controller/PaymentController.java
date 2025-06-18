package com.sample.sample.Controller;

import com.razorpay.RazorpayException;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.Payment;
import com.sample.sample.Service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create/{accountId}/{addressId}")
    public ResponseEntity<Payment> createOrder(
            @PathVariable Long accountId,@PathVariable Long addressId,@RequestBody PaymentRequestDTO request) throws RazorpayException {

        return ResponseEntity.ok(paymentService.createOrderPayment(accountId,addressId,request));
    }


    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentRequestDTO dto) {
        boolean isValid = paymentService.verifyPayment(dto);
        if (isValid) {
            return ResponseEntity.ok("Payment verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment signature.");
        }
    }

    @GetMapping("/all")
    public List<?> getAllPayments(){
        return paymentService.getAllPayments();
    }


}

