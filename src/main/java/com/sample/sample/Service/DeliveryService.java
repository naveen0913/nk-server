package com.sample.sample.Service;

import com.sample.sample.DTO.DeliveryRequest;
import com.sample.sample.DTO.LoginDTO;
import com.sample.sample.JWT.JwtUtil;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.DeliveryAgentRepo;
import com.sample.sample.Repository.DeliveryRepo;
import com.sample.sample.Repository.OrderRepository;
import com.sample.sample.Responses.AgentResponse;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.DeliveryResponse;
import com.sample.sample.configuration.DistanceUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private JwtUtil jwtUtil;

    private final Random random = new Random();
    private static final double DEFAULT_LATITUDE = 17.4000; // example warehouse
    private static final double DEFAULT_LONGITUDE = 78.4000;

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
//        delivery.setCurrentLatitude(req.getLatitude());
//        delivery.setCurrentLongitude(req.getLongitude());

        // Set initial location automatically
        double initialLat = agent.getLatitude() !=null  ? agent.getLatitude() : DEFAULT_LATITUDE;
        double initialLng = agent.getLongitude() != null ? agent.getLongitude() : DEFAULT_LONGITUDE;
        delivery.setCurrentLatitude(initialLat);
        delivery.setCurrentLongitude(initialLng);
        delivery.setCurrentLatitude(initialLat);
        delivery.setCurrentLongitude(initialLng);

        Delivery saved = deliveryRepository.save(delivery);
        agent.setLatitude(initialLat);
        agent.setLongitude(initialLng);
        deliveryAgentRepo.save(agent);
        return mapToResponse(saved, order);
    }

    @Scheduled(fixedRate = 30000)
    public void autoUpdateLocations() {
        List<Delivery> deliveries = deliveryRepository.findAllAssignedDeliveries();

        for (Delivery delivery : deliveries) {
            double newLat = delivery.getCurrentLatitude() + (random.nextDouble() - 0.5) / 100;
            double newLng = delivery.getCurrentLongitude() + (random.nextDouble() - 0.5) / 100;

            delivery.setCurrentLatitude(newLat);
            delivery.setCurrentLongitude(newLng);

            Delivery updated = deliveryRepository.save(delivery);

            // Push update via WebSocket
            simpMessagingTemplate.convertAndSend(
                    "/topic/delivery/" + delivery.getTrackingId(),
                    mapToResponse(updated, updated.getOrder())
            );
        }
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


    public DeliveryResponse getDeliveryStatus(String orderId) {
        Delivery delivery = deliveryRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        return mapToResponse(delivery, delivery.getOrder());
    }

    public AuthResponse addNewDeliveryAgent(String name,String email,String phone,boolean status,String password){
        DeliveryAgent deliveryAgent = new DeliveryAgent();
        deliveryAgent.setAgentName(name);
        deliveryAgent.setEmail(email);
        deliveryAgent.setPhone(phone);
        deliveryAgent.setActive(status);
        deliveryAgent.setPassword(password);
        deliveryAgentRepo.save(deliveryAgent);
        return new AuthResponse(HttpStatus.CREATED.value(), "created",null);
    }

    public AuthResponse getAllDeliveryAgents(){
        List<DeliveryAgent> deliveryAgents  = deliveryAgentRepo.findAll();
        if (deliveryAgents.isEmpty()){
            return new AuthResponse(HttpStatus.NOT_FOUND.value(),"no agents right now",null);
        }
        return new AuthResponse(HttpStatus.OK.value(),"no agents right now",deliveryAgents);
    }

    public AuthResponse agentLogin(LoginDTO dto) {
        Optional<DeliveryAgent> existedAgent = deliveryAgentRepo.findByEmail(dto.getEmail());
        if (existedAgent.isEmpty()){
            return new AuthResponse(HttpStatus.NOT_FOUND.value(),"no agents right now",null);
        }
        DeliveryAgent agent = existedAgent.get();
        if (!dto.getPassword().equals(agent.getPassword())) {
            return new AuthResponse(HttpStatus.BAD_REQUEST.value(), "Incorrect password! Try again", null);
        }
        String token = jwtUtil.generateToken(agent.getEmail());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("user", agent);
        responseBody.put("token", token);
        return new AuthResponse(HttpStatus.OK.value(), "Login success", responseBody);
    }

    public AuthResponse getUserFromRequest(HttpServletRequest request) {
        try {
            // 1. Get JWT token from cookie
            String token = extractJwtFromCookies(request);
            if (token == null || token.isEmpty()) {
                return new AuthResponse(HttpStatus.UNAUTHORIZED.value(), "Missing token", null);
            }

            String email = jwtUtil.extractEmail(token);
            if (email == null || !jwtUtil.validateToken(token, email)) {
                return new AuthResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired token", null);
            }

            DeliveryAgent existed = deliveryAgentRepo.findByEmail(email).orElse(null);
            if (existed == null) {
                return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Agent not found", null);
            }

            return new AuthResponse(HttpStatus.OK.value(), "Success", existed);

        } catch (Exception e) {
            e.printStackTrace();
            return new AuthResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: " + e.getMessage(), null);
        }
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }



}
