package com.sample.sample.Repository;


import com.sample.sample.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    Payment findByOrderId(String orderId);

    Optional<Payment> findByOrderId(String orderId);
}

