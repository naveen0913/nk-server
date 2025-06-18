package com.sample.sample.Service;

import com.sample.sample.Model.*;
import com.sample.sample.Repository.AccountDetailsRepository;
import com.sample.sample.Repository.CartItemRepository;
import com.sample.sample.Repository.OrderRepository;
import com.sample.sample.Repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


//    public Orders placeOrder(Long accountId, Long addressId, OrderDTO request) {
//        AccountDetails account = accountDetailsRepository.findById(accountId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));
//
//        UserAddress address = userAddressRepository.findById(addressId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User not found with ID: " + addressId));
//
//        List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());
//
//        String generatedPaymentId = "pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
//
//        Orders order = new Orders();
////        order.setAccountDetails(account);
////        order.setUserAddress(address);
//        order.setOrderDiscount(request.getOrderDiscount());
//        order.setOrderTotal(request.getOrderTotal());
////        order.setCartItemList(cartItems);
//        order.setOrderGstPercent(request.getOrderGstPercent());
//        order.setOrderShippingCharges(request.getShippingCharges());
//        order.setOrderStatus("PLACED");
//        order.setCreatedAt(new Date());
//
//        Payment payment = new Payment();
//        payment.setPaymentId(generatedPaymentId);
//        payment.setStatus(PaymentStatus.PENDING);
//        payment.setCurrency("INR");
//        payment.setAmount(request.getOrderTotal());
//        payment.setReceipt("rcpt_" + UUID.randomUUID().toString().substring(0, 8));
//        payment.setOrder(order);
//
//        order.setPayment(payment);
////        mailService.sendOrderPlacedMail(account.setAccountEmail(),order.getOrderId());
//
//        return orderRepository.save(order);
//    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Orders> getOrdersByAccount(Long accountId) {
        AccountDetails account = accountDetailsRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        return orderRepository.findAllByAccountId(accountId);
    }

    public Optional<Orders> getOrderById(Long orderId){
        return orderRepository.findById(orderId);
    }

}
