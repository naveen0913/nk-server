package com.sample.sample.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.Orders;
import com.sample.sample.Model.Payment;
import com.sample.sample.Model.PaymentStatus;
import com.sample.sample.Repository.OrderRepository;
import com.sample.sample.Repository.PaymentRepository;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;


@Service
public class PaymentService {

    @Value("${razorpay.api.key}")
    private String key;

    @Value("${razorpay.api.secret}")
    private String secret;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createOrder(Long orderId) throws RazorpayException {

        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        Payment payment = orders.getPayment();
        String generatedPaymentId = "pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        if (payment == null) {
            payment = new Payment();
            payment.setAmount(orders.getOrderTotal());
            payment.setCurrency("INR");
            payment.setPaymentId(generatedPaymentId);
            payment.setReceipt("rcpt_" + UUID.randomUUID().toString().substring(0, 8));
            payment.setStatus(PaymentStatus.PENDING);
            payment.setOrder(orders);
            orders.setPayment(payment);
        }


            RazorpayClient razorpay = new RazorpayClient(key, secret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", payment.getAmount()*100);
            orderRequest.put("currency", payment.getCurrency());
            orderRequest.put("receipt", payment.getReceipt());

            Order razorpayOrder = razorpay.orders.create(orderRequest);

            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            payment.setAmount(payment.getAmount());
            payment.setCurrency(payment.getCurrency());
            payment.setReceipt(payment.getReceipt());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setOrder(orders);
            payment.setSignature("");
            payment.setPaymentId(payment.getPaymentId());
            return paymentRepository.save(payment);

    }

    public boolean verifyPayment(PaymentRequestDTO dto) {
        try {
            String payload = dto.getOrderId() + "|" + dto.getPaymentId();
            String generatedSignature = hmacSHA256(payload, secret);

            if (generatedSignature.equals(dto.getSignature())) {
                Payment payment = paymentRepository.findByRazorpayOrderId(dto.getOrderId())
                        .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + dto.getOrderId()));

                payment.setPaymentId(dto.getPaymentId());
                payment.setSignature(dto.getSignature());
                payment.setStatus(PaymentStatus.SUCCESS);
                paymentRepository.save(payment);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String hmacSHA256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

}
