package com.sample.sample.Service;

import com.sample.sample.Repository.ProductRepository;
import com.sample.sample.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepo userRepository;

    public long getTotalProducts() {
        return productRepository.count();
    }

    public long getTotalUsers() {
        return userRepository.count();
    }
}
