package com.sample.sample.Service;

import com.sample.sample.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private DesignRepo designRepo;

    public long getTotalProducts() {
        return productsRepository.count();
    }

    public long getTotalUsers() {
        return userRepository.countByRole("user");
    }

    public long getTotalOrders() {
        return orderRepository.count();
    }

    public long getTotalPayments(){
        return paymentRepository.count();
    }

    public long getTotalCartItems(){
        return cartItemRepository.count();
    }

    public long getTotalAddresses(){
        return userAddressRepository.count();
    }

    public long getTotalDesigns(){
        return designRepo.count();
    }

}
