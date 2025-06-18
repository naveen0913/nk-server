package com.sample.sample.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sample.sample.DTO.OrderDTO;
import com.sample.sample.DTO.PaymentRequestDTO;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.*;
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
import java.util.Date;
import java.util.List;
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

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    MailService mailService;

    public Payment createOrderPayment(Long accountId, Long addressId, PaymentRequestDTO request)throws RazorpayException{

        AccountDetails account = accountDetailsRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));

        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with ID: " + addressId));

        List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());
        String generatedPaymentId = "pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        RazorpayClient razorpay = new RazorpayClient(key, secret);
        Payment payment = new Payment();
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", request.getAmount()*100);
        orderRequest.put("currency", request.getCurrency());
        orderRequest.put("receipt", request.getReceipt());

        Order razorpayOrder = razorpay.orders.create(orderRequest);

        payment.setRazorpayOrderId(razorpayOrder.get("id"));
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setReceipt("rcpt_" + UUID.randomUUID().toString().substring(0, 8));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setSignature("");
        payment.setPaymentId(generatedPaymentId);
        payment.setCartItemList(cartItems);
        payment.setAccountDetails(account);
        payment.setUserAddress(address);

        return paymentRepository.save(payment);

    }




//    public Payment createOrder(Long orderId) throws RazorpayException {
//
//        Orders orders = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
//
//        Payment payment = orders.getPayment();
//        String generatedPaymentId = "pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
//
//        if (payment == null) {
//            payment = new Payment();
//            payment.setAmount(orders.getOrderTotal());
//            payment.setCurrency("INR");
//            payment.setPaymentId(generatedPaymentId);
//            payment.setReceipt("rcpt_" + UUID.randomUUID().toString().substring(0, 8));
//            payment.setStatus(PaymentStatus.PENDING);
//            payment.setOrder(orders);
//            orders.setPayment(payment);
//        }
//
//
//            RazorpayClient razorpay = new RazorpayClient(key, secret);
//            JSONObject orderRequest = new JSONObject();
//            orderRequest.put("amount", payment.getAmount()*100);
//            orderRequest.put("currency", payment.getCurrency());
//            orderRequest.put("receipt", payment.getReceipt());
//
//            Order razorpayOrder = razorpay.orders.create(orderRequest);
//
//            payment.setRazorpayOrderId(razorpayOrder.get("id"));
//            payment.setAmount(payment.getAmount());
//            payment.setCurrency(payment.getCurrency());
//            payment.setReceipt(payment.getReceipt());
//            payment.setStatus(PaymentStatus.PENDING);
//            payment.setOrder(orders);
//            payment.setSignature("");
//            payment.setPaymentId(payment.getPaymentId());
//            return paymentRepository.save(payment);
//    }



    public boolean verifyPayment(PaymentRequestDTO dto) {
        try {
            String payload = dto.getRazorpayOrderId() + "|" + dto.getPaymentId();
            String generatedSignature = hmacSHA256(payload, secret);

            if (generatedSignature.equals(dto.getSignature())) {
                Payment payment = paymentRepository.findByRazorpayOrderId(dto.getRazorpayOrderId())
                        .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + dto.getRazorpayOrderId()));

                payment.setPaymentId(dto.getPaymentId());
                payment.setSignature(dto.getSignature());
                payment.setStatus(PaymentStatus.SUCCESS);
                paymentRepository.save(payment);
                createOrderAfterPayment(payment);

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

    public List<Payment> getAllPayments(){
      return paymentRepository.findAll();
    }

    private void createOrderAfterPayment(Payment payment) {
        Orders order = new Orders();
        order.setCreatedAt(new Date());
        order.setOrderStatus("PLACED");
        order.setOrderTotal(payment.getAmount());
        order.setOrderDiscount("0");
        order.setOrderGstPercent(String.valueOf(payment.getGstAmount()));
        order.setOrderShippingCharges(String.valueOf(payment.getShippingPrice()));

        // Save the order first
        Orders savedOrder = orderRepository.save(order);

        // Now attach order to payment
        payment.setOrder(savedOrder);
        paymentRepository.save(payment);

        // Send order confirmation email
        String email = payment.getAccountDetails().getAccountEmail();
        String orderNumber = String.valueOf(order.getOrderId());

        mailService.sendOrderPlacedMail(email, orderNumber);
    }


}
