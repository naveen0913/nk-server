package com.sample.sample.Repository;

import com.sample.sample.Model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery,Long> {
    Optional<Delivery> findByTrackingId(String trackingId);
    Optional<Delivery> findByOrder_OrderId(String orderId);
}
