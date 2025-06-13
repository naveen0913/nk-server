package com.sample.sample.Service;

import com.sample.sample.Repository.ImageRepo;
import com.sample.sample.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserRepo userRepository;

    public long getTotalProducts() {
        return imageRepo.count();
    }

//    public long getTotalUsers() {
//        return userRepository.countByRole("user");
//    }
}
