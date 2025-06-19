package com.example.demo.service;

import com.example.demo.model.OrderTracking;
import com.example.demo.repository.OrderTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderTrackingService {

    @Autowired
    private OrderTrackingRepository orderTrackingRepository;

    public OrderTracking saveTracking(Long orderId, OrderTracking tracking) {
        String generatedaTrackingId = "track_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        tracking.setTrackingId(generatedaTrackingId);
        return orderTrackingRepository.save(tracking);
    }

    public List<OrderTracking> getAllTrackings() {
        return orderTrackingRepository.findAll();
    }

    public Optional<OrderTracking> getTrackingById(Long id) {
        return orderTrackingRepository.findById(id);
    }


    public OrderTracking updateTracking(Long id, OrderTracking updatedTracking) {
        return orderTrackingRepository.findById(id).map(existingTracking -> {
            existingTracking.setTrackingId(updatedTracking.getTrackingId());
            existingTracking.setTrackingStatus(updatedTracking.getTrackingStatus());
            existingTracking.setOrder(updatedTracking.getOrder());
            existingTracking.setIsShipped(updatedTracking.getIsShipped());
            existingTracking.setIsPacked(updatedTracking.getIsPacked());
            existingTracking.setOutOfDelivery(updatedTracking.getOutOfDelivery());
            existingTracking.setDelivered(updatedTracking.getDelivered());
            return orderTrackingRepository.save(existingTracking);
        }).orElseThrow(() -> new RuntimeException("Tracking not found with id: " + id));
    }
}
