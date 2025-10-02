package com.sample.sample.Repository;

import com.sample.sample.Model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery,Long> {
    Optional<Delivery> findByTrackingId(String trackingId);
    Optional<Delivery> findByOrder_OrderId(String orderId);

    @Query("SELECT d FROM Delivery d WHERE d.deliveryStatus = 'ASSIGNED'")
    List<Delivery> findAllAssignedDeliveries();
}
