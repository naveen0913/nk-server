package com.sample.sample.Repository;

import com.sample.sample.Model.AccountDetails;
import com.sample.sample.Model.OrderStatus;
import com.sample.sample.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o WHERE o.payment.accountDetails.id = :accountId")
    List<Orders> findAllByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT o FROM Orders o WHERE o.user.id = :userId")
    List<Orders> findAllByUserId(@Param("userId") String userId);


    Optional<Orders> findByOrderId(String orderId);

    @Query("SELECT COUNT(o) FROM Orders o WHERE DATE(o.createdAt) = CURRENT_DATE")
    long countTodayOrders();

    Optional<Orders> findBySessionIdAndStatus(String sessionId, OrderStatus status);
    Optional<Orders> findByIdAndSessionId(Long id, String sessionId);



}
