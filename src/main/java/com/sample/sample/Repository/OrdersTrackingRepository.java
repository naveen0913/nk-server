package com.sample.sample.Repository;

import com.sample.sample.Model.OrdersTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersTrackingRepository extends JpaRepository<OrdersTracking,Long> {
}
