package com.sample.sample.Service;

import com.sample.sample.DTO.DeliveryRequest;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.DeliveryAgentRepo;
import com.sample.sample.Repository.DeliveryRepo;
import com.sample.sample.Repository.OrderRepository;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.DeliveryResponse;
import com.sample.sample.configuration.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepo deliveryRepository;

    @Autowired
    private DeliveryAgentRepo deliveryAgentRepo;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public DeliveryResponse assignDeliveryAgent(DeliveryRequest req) {
        Orders order = orderRepository.findByOrderId(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        DeliveryAgent agent = deliveryAgentRepo.findById(req.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        Delivery delivery = new Delivery();
        delivery.setTrackingId(UUID.randomUUID().toString());
        delivery.setOrder(order);
        delivery.setAgent(agent);
        delivery.setDeliveryStatus("ASSIGNED");
        delivery.setAssignedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        delivery.setCurrentLatitude(req.getLatitude());
        delivery.setCurrentLongitude(req.getLongitude());

        Delivery saved = deliveryRepository.save(delivery);

        return mapToResponse(saved, order);
    }

    public DeliveryResponse updateLocation(String trackingId, double lat, double lng, String status) {
        Delivery delivery = deliveryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setCurrentLatitude(lat);
        delivery.setCurrentLongitude(lng);
        delivery.setDeliveryStatus(status);

        if ("DELIVERED".equals(status)) {
            delivery.setDeliveredAt(LocalDateTime.now());
        }

        Delivery updated = deliveryRepository.save(delivery);

        // Push to WebSocket
        simpMessagingTemplate.convertAndSend("/topic/delivery/" + trackingId, mapToResponse(updated, updated.getOrder()));

        return mapToResponse(updated, updated.getOrder());
    }

    public DeliveryResponse getDeliveryStatus(String orderId) {
        Delivery delivery = deliveryRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        return mapToResponse(delivery, delivery.getOrder());
    }

    private DeliveryResponse mapToResponse(Delivery delivery, Orders order) {
        DeliveryResponse dr = new DeliveryResponse();
        dr.setTrackingId(delivery.getTrackingId());
        dr.setStatus(delivery.getDeliveryStatus());
        dr.setLatitude(delivery.getCurrentLatitude());
        dr.setLongitude(delivery.getCurrentLongitude());
        dr.setAgentName(delivery.getAgent().getAgentName());
        dr.setAgentPhone(delivery.getAgent().getPhone());
        dr.setTrackingWebSocketUrl("ws://localhost:8083/ws/topic/delivery/" + delivery.getTrackingId());

        UserAddress ua = order.getUserAddress();
        if (ua != null && ua.getLatitude() != null && ua.getLongitude() != null) {

            double distance = DistanceUtils.calculateDistance(
                    delivery.getCurrentLatitude(),
                    delivery.getCurrentLongitude(),
                    ua.getLatitude(),
                    ua.getLongitude()
            );
            dr.setEstimatedMinutes(DistanceUtils.calculateETA(distance, 30.0));
        }
        return dr;
    }

    public AuthResponse addNewDeliveryAgent(String name,String email,String phone,boolean status){
        DeliveryAgent deliveryAgent = new DeliveryAgent();
        deliveryAgent.setAgentName(name);
        deliveryAgent.setEmail(email);
        deliveryAgent.setPhone(phone);
        deliveryAgent.setActive(status);
        deliveryAgentRepo.save(deliveryAgent);
        return new AuthResponse(HttpStatus.CREATED.value(), "created",null);
    }

    public AuthResponse getAllDeliveryAgents(){
        List<DeliveryAgent> deliveryAgents = deliveryAgentRepo.findAll();
        if (deliveryAgents.isEmpty()){
            return new AuthResponse(HttpStatus.NOT_FOUND.value(),"no agents right now",null);
        }
        return new AuthResponse(HttpStatus.OK.value(),"no agents right now",deliveryAgents);
    }

}
