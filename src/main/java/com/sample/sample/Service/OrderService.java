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
