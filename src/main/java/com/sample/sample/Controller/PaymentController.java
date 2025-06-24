package com.sample.sample.Controller;

import com.razorpay.RazorpayException;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.Orders;
import com.sample.sample.Model.Payment;
import com.sample.sample.Repository.PaymentRepository;
import com.sample.sample.Responses.PaymentResponse;
import com.sample.sample.Service.OrdersTrackingService;
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

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/create/{accountId}/{addressId}")
    public ResponseEntity<Payment> createOrder(
            @PathVariable Long accountId,@PathVariable Long addressId,@RequestBody PaymentRequestDTO request) throws RazorpayException {

        return ResponseEntity.ok(paymentService.createOrderPayment(accountId,addressId,request));
    }


    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentRequestDTO dto) {
        boolean isValid = paymentService.verifyPayment(dto);
        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK.value()).body("Payment Verified Successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body("Invalid payment signature.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByAccount(@PathVariable Long accountId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByAccount(accountId);
        return ResponseEntity.ok(payments);
    }

}

