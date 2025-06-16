package com.sample.sample.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sample.sample.Model.Payment;
import com.sample.sample.Model.PaymentStatus;
import com.sample.sample.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${razorpay.api.key}")
    private String key;

    @Value("${razorpay.api.secret}")
    private String secret;

    private final PaymentRepository paymentRepository;

    /**
     * Create Razorpay Order and save it in the database
     */
    public Payment createOrder(int amount, String currency, String receipt) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(key, secret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Amount in paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        Order order = razorpay.orders.create(orderRequest);

        Payment payment = new Payment();
        payment.setOrderId(order.get("id"));
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setReceipt(receipt);
        payment.setStatus(PaymentStatus.FAILED); // Default status, will update after verification

        return paymentRepository.save(payment);
    }

    /**
     * Verify Razorpay Payment and update the status
     */
    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        String generatedSignature = HmacSHA256(orderId + "|" + paymentId, secret);

        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();

            if (generatedSignature.equals(signature)) {
                payment.setPaymentId(paymentId);
                payment.setSignature(signature);
                payment.setStatus(PaymentStatus.SUCCESS);
                paymentRepository.save(payment);
                return true;
            } else {
                payment.setPaymentId(paymentId);
                payment.setSignature(signature);
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                return false;
            }
        }
        return false;
    }

    /**
     * HMAC SHA256 signature generation
     */
    private String HmacSHA256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            byte[] hmacData = mac.doFinal(data.getBytes());
            return javax.xml.bind.DatatypeConverter.printHexBinary(hmacData).toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC SHA256", e);
        }
    }
}
