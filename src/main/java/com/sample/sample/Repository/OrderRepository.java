package com.sample.sample.Repository;

import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders,Long> {
@Query("SELECT o FROM Orders o WHERE o.payment.accountDetails.id = :accountId")
List<Orders> findAllByAccountId(@Param("accountId") Long accountId);
}
