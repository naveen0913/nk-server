package com.sample.sample.Repository;


import com.sample.sample.Model.Payment;
import org.apache.juli.logging.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") Long orderId);

    List<Payment> findByAccountDetails_Id(Long accountId);

    @Query("SELECT COUNT(o) FROM Payment o WHERE DATE(o.paymentPaidDate) = CURRENT_DATE")
    long countTodayPayments();

}

