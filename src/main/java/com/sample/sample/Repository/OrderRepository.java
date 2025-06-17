package com.sample.sample.Repository;

import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByAccountDetails(AccountDetails accountDetails);
//    Optional<Orders> findByOrderId(String orderId);
}
