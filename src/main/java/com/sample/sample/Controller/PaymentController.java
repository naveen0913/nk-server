package com.sample.sample.Controller;

import com.razorpay.RazorpayException;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Repository.PaymentRepository;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.PaymentResponse;
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

//    @PostMapping("/create/{accountId}/{addressId}")
//    public ResponseEntity<AuthResponse> createOrder(
//            @PathVariable Long accountId,
//            @PathVariable Long addressId,
//            @RequestBody PaymentRequestDTO request) throws RazorpayException {
//
//        Payment payment = paymentService.createOrderPayment(accountId, addressId, request);
//        AuthResponse response = new AuthResponse(HttpStatus.OK.value(), "Order payment created successfully", payment);
//        return ResponseEntity.ok(response);
//    }


    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentRequestDTO dto) {
        boolean isValid = paymentService.verifyPayment(dto);
        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK.value()).body("Payment Verified Successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body("Invalid payment signature.");
        }
    }

//    @GetMapping("/all")
//    public ResponseEntity<AuthResponse> getAllPayments() {
//        List<PaymentResponse> payments = paymentService.getAllPayments();
//        AuthResponse response = new AuthResponse(HttpStatus.OK.value(), "Payments fetched successfully", payments);
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("/all")
    public ResponseEntity<AuthResponse> getAllPayments() {
        AuthResponse response = paymentService.getAllPayments();
        return ResponseEntity.ok(response);
    }


//    @GetMapping("/account/{accountId}")
//    public ResponseEntity<AuthResponse> getPaymentsByAccount(@PathVariable Long accountId) {
//        AuthResponse response = paymentService.getPaymentsByAccount(accountId);
//        return ResponseEntity.ok(response);
//    }



    @PostMapping("/create/{accountId}/{addressId}")
    public ResponseEntity<AuthResponse> createOrderPayment(
            @PathVariable Long accountId,
            @PathVariable Long addressId,
            @RequestBody PaymentRequestDTO request) throws RazorpayException {

        AuthResponse response = paymentService.createOrderPayment(accountId, addressId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



//    @GetMapping("/account/{accountId}")
//    public ResponseEntity<List<PaymentResponse>> getPaymentsByAccount(@PathVariable Long accountId) {
//        List<PaymentResponse> payments = paymentService.getPaymentsByAccount(accountId);
//        return ResponseEntity.ok(payments);
//    }


}

