package com.sample.sample.Service;

import com.sample.sample.Model.*;
import com.sample.sample.Repository.AccountDetailsRepository;
import com.sample.sample.Repository.CartItemRepository;
import com.sample.sample.Repository.OrderRepository;
import com.sample.sample.Repository.UserAddressRepository;
import com.sample.sample.Responses.AccountDetailsResponse;
import com.sample.sample.Responses.OrdersResponse;
import com.sample.sample.Responses.PaymentResponse;
import com.sample.sample.Responses.UserAddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MailService mailService;


    public List<OrdersResponse> getAllOrders() {
        List<Orders> orders = orderRepository.findAll();

        return orders.stream().map(order -> {

            // --- Account Details Mapping ---
            AccountDetails acc = order.getPayment().getAccountDetails();
            AccountDetailsResponse accountDTO = new AccountDetailsResponse();
            accountDTO.setId(acc.getId());
            accountDTO.setFirstName(acc.getFirstName());
            accountDTO.setLastName(acc.getLastName());
            accountDTO.setPhone(acc.getPhone());
            accountDTO.setAccountEmail(acc.getAccountEmail());
            accountDTO.setAlternatePhone(acc.getAlternatePhone());

            // --- Payment Mapping ---
            Payment payment = order.getPayment();
            PaymentResponse paymentDTO = new PaymentResponse();
            paymentDTO.setId(payment.getId());
            paymentDTO.setRazorpayOrderId(payment.getRazorpayOrderId());
            paymentDTO.setPaymentId(payment.getPaymentId());
            paymentDTO.setSignature(payment.getSignature());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setGstAmount(payment.getGstAmount());
            paymentDTO.setShippingPrice(payment.getShippingPrice());
            paymentDTO.setCurrency(payment.getCurrency());
            paymentDTO.setReceipt(payment.getReceipt());
            paymentDTO.setStatus(payment.getStatus().toString());
            paymentDTO.setAccountDetails(accountDTO);

            // --- User Address Mapping ---
            UserAddress ua = order.getUserAddress();
            UserAddressResponse userAddressDTO = null;
            if (ua != null) {
                userAddressDTO = new UserAddressResponse();
                userAddressDTO.setAddressId(ua.getAddressId());
                userAddressDTO.setFirstName(ua.getFirstName());
                userAddressDTO.setLastName(ua.getLastName());
                userAddressDTO.setPhone(ua.getPhone());
                userAddressDTO.setAddressType(ua.getAddressType());
                userAddressDTO.setAddressLine1(ua.getAddressLine1());
                userAddressDTO.setAddressLine2(ua.getAddressLine2());
                userAddressDTO.setCity(ua.getCity());
                userAddressDTO.setState(ua.getState());
                userAddressDTO.setCountry(ua.getCountry());
                userAddressDTO.setPincode(ua.getPincode());
            }

            // --- Final Order Response Mapping ---
            OrdersResponse dto = new OrdersResponse();
            dto.setId(order.getId());
            dto.setOrderId(order.getOrderId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setOrderStatus(order.getOrderStatus());
            dto.setOrderTotal(order.getOrderTotal());
            dto.setOrderDiscount(order.getOrderDiscount());
            dto.setOrderGstPercent(order.getOrderGstPercent());
            dto.setOrderShippingCharges(order.getOrderShippingCharges());
            dto.setOrderItems(order.getOrderItems());
            dto.setOrderTracking(order.getOrderTracking());
            dto.setPayment(paymentDTO);
            dto.setAccountDetails(accountDTO);
            dto.setUserAddress(userAddressDTO);

            return dto;
        }).collect(Collectors.toList());
    }


    public List<OrdersResponse> getOrdersByAccountId(Long accountId) {
        List<Orders> orders = orderRepository.findAllByAccountId(accountId);

        return orders.stream().map(order -> {

            AccountDetails acc = order.getPayment().getAccountDetails();
            AccountDetailsResponse accountDTO = new AccountDetailsResponse();
            accountDTO.setId(acc.getId());
            accountDTO.setFirstName(acc.getFirstName());
            accountDTO.setLastName(acc.getLastName());
            accountDTO.setPhone(acc.getPhone());
            accountDTO.setAccountEmail(acc.getAccountEmail());
            accountDTO.setAlternatePhone(acc.getAlternatePhone());

            UserAddress ua = order.getUserAddress();
            UserAddressResponse userAddressDTO = null;
            if (ua != null) {
                userAddressDTO = new UserAddressResponse();
                userAddressDTO.setAddressId(ua.getAddressId());
                userAddressDTO.setFirstName(ua.getFirstName());
                userAddressDTO.setLastName(ua.getLastName());
                userAddressDTO.setPhone(ua.getPhone());
                userAddressDTO.setAddressType(ua.getAddressType());
                userAddressDTO.setAddressLine1(ua.getAddressLine1());
                userAddressDTO.setAddressLine2(ua.getAddressLine2());
                userAddressDTO.setCity(ua.getCity());
                userAddressDTO.setState(ua.getState());
                userAddressDTO.setCountry(ua.getCountry());
                userAddressDTO.setPincode(ua.getPincode());
            }

            // --- Build payment response ---
            Payment payment = order.getPayment();
            PaymentResponse paymentDTO = new PaymentResponse();
            paymentDTO.setId(payment.getId());
            paymentDTO.setRazorpayOrderId(payment.getRazorpayOrderId());
            paymentDTO.setPaymentId(payment.getPaymentId());
            paymentDTO.setSignature(payment.getSignature());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setGstAmount(payment.getGstAmount());
            paymentDTO.setShippingPrice(payment.getShippingPrice());
            paymentDTO.setCurrency(payment.getCurrency());
            paymentDTO.setReceipt(payment.getReceipt());
            paymentDTO.setStatus(payment.getStatus().toString());
            paymentDTO.setAccountDetails(accountDTO);

            // --- Build final response ---
            OrdersResponse dto = new OrdersResponse();
            dto.setId(order.getId());
            dto.setOrderId(order.getOrderId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setOrderStatus(order.getOrderStatus());
            dto.setOrderTotal(order.getOrderTotal());
            dto.setOrderItems(order.getOrderItems());
            dto.setOrderTracking(order.getOrderTracking());
            dto.setOrderDiscount(order.getOrderDiscount());
            dto.setOrderGstPercent(order.getOrderGstPercent());
            dto.setOrderShippingCharges(order.getOrderShippingCharges());
            dto.setAccountDetails(accountDTO);
            dto.setUserAddress(userAddressDTO);
            dto.setPayment(paymentDTO);

            return dto;
        }).collect(Collectors.toList());
    }

    public OrdersResponse getOrderByOrderId(String orderId) {
        Orders order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + orderId));

        AccountDetails acc = order.getPayment().getAccountDetails();
        AccountDetailsResponse accountDTO = new AccountDetailsResponse();
        accountDTO.setId(acc.getId());
        accountDTO.setFirstName(acc.getFirstName());
        accountDTO.setLastName(acc.getLastName());
        accountDTO.setPhone(acc.getPhone());
        accountDTO.setAccountEmail(acc.getAccountEmail());
        accountDTO.setAlternatePhone(acc.getAlternatePhone());

        // Map Payment
        Payment payment = order.getPayment();
        PaymentResponse paymentDTO = new PaymentResponse();
        paymentDTO.setId(payment.getId());
        paymentDTO.setRazorpayOrderId(payment.getRazorpayOrderId());
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setSignature(payment.getSignature());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setGstAmount(payment.getGstAmount());
        paymentDTO.setShippingPrice(payment.getShippingPrice());
        paymentDTO.setCurrency(payment.getCurrency());
        paymentDTO.setReceipt(payment.getReceipt());
        paymentDTO.setStatus(payment.getStatus().toString());
        paymentDTO.setAccountDetails(accountDTO);

        // Map UserAddress
        UserAddress ua = order.getUserAddress();
        UserAddressResponse userAddressDTO = null;
        if (ua != null) {
            userAddressDTO = new UserAddressResponse();
            userAddressDTO.setAddressId(ua.getAddressId());
            userAddressDTO.setFirstName(ua.getFirstName());
            userAddressDTO.setLastName(ua.getLastName());
            userAddressDTO.setPhone(ua.getPhone());
            userAddressDTO.setAddressType(ua.getAddressType());
            userAddressDTO.setAddressLine1(ua.getAddressLine1());
            userAddressDTO.setAddressLine2(ua.getAddressLine2());
            userAddressDTO.setCity(ua.getCity());
            userAddressDTO.setState(ua.getState());
            userAddressDTO.setCountry(ua.getCountry());
            userAddressDTO.setPincode(ua.getPincode());
        }

        // Map to DTO
        OrdersResponse dto = new OrdersResponse();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setOrderTotal(order.getOrderTotal());
        dto.setOrderDiscount(order.getOrderDiscount());
        dto.setOrderGstPercent(order.getOrderGstPercent());
        dto.setOrderShippingCharges(order.getOrderShippingCharges());
        dto.setOrderItems(order.getOrderItems());
        dto.setOrderTracking(order.getOrderTracking());
        dto.setPayment(paymentDTO);
        dto.setAccountDetails(accountDTO);
        dto.setUserAddress(userAddressDTO);

        return dto;
    }

}
