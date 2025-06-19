package com.sample.sample.Service;

import com.sample.sample.Model.Orders;
import com.sample.sample.Model.OrdersTracking;
import com.sample.sample.Repository.OrderRepository;
import com.sample.sample.Repository.OrdersTrackingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
public class OrdersTrackingService {

    @Autowired
    private OrdersTrackingRepository ordersTrackingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public OrdersTracking saveTracking(Long orderId, OrdersTracking dto) {

        Orders existedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found with ID: " + orderId));

        // Create tracking ID
        String generatedTrackingId = "track_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        OrdersTracking tracking = new OrdersTracking();
        tracking.setTrackingId(generatedTrackingId);
        tracking.setTrackingStatus(dto.getTrackingStatus());
        tracking.setShipped(dto.getShipped());
        tracking.setPacked(dto.getPacked());
        tracking.setOutOfDelivery(dto.getOutOfDelivery());
        tracking.setDelivered(dto.getDelivered());
        tracking.setTrackingCreated(new Date());

        tracking.setOrder(existedOrder);
        return ordersTrackingRepository.save(tracking);
    }

}
