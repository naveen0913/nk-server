package com.sample.sample.Service;

import com.sample.sample.DTO.NotificationDTO;
import com.sample.sample.DTO.OrderTrackingDTO;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.OrderRepository;
import com.sample.sample.Repository.OrdersTrackingRepository;
import com.sample.sample.Responses.AuthResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersTrackingService {

    @Autowired
    private OrdersTrackingRepository ordersTrackingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public AuthResponse getOrderTrackingById(Long orderId) {
        Orders existedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found with ID: " + orderId));

        Optional<OrdersTracking> ordersTracking = ordersTrackingRepository.findById(existedOrder.getId());

        if (ordersTracking.isPresent()) {
            return new AuthResponse(HttpStatus.OK.value(), "Order tracking fetched successfully", ordersTracking.get());
        } else {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Tracking info not found for order ID: " + orderId, null);
        }
    }


    public AuthResponse getAllOrdersTracking() {
        List<OrdersTracking> trackingList = ordersTrackingRepository.findAll();
        return new AuthResponse(HttpStatus.OK.value(), "All order tracking data fetched successfully", trackingList);
    }


    public AuthResponse getTrackingById(Long trackingId) {
        Optional<OrdersTracking> existedTracking = ordersTrackingRepository.findById(trackingId);

        if (!existedTracking.isPresent()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Order Tracking not found with ID: " + trackingId, null);
        }

        return new AuthResponse(HttpStatus.OK.value(), "Order Tracking fetched successfully", existedTracking.get());
    }


    @Transactional
    public AuthResponse updateTracking(Long trackingId, OrderTrackingDTO dto) {
        OrdersTracking existing = ordersTrackingRepository.findById(trackingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tracking not found with ID: " + trackingId));


        if (dto.getTrackingStatus() != null) {
            existing.setTrackingStatus(dto.getTrackingStatus());
        }
        if (dto.getShipped() != null) {
            existing.setShipped(dto.getShipped());
        }
        if (dto.getPacked() != null) {
            existing.setPacked(dto.getPacked());
        }
        if (dto.getOutOfDelivery() != null) {
            existing.setOutOfDelivery(dto.getOutOfDelivery());
        }
        if (dto.getDelivered() != null) {
            existing.setDelivered(dto.getDelivered());
        }
        if (dto.getEstimatedDelivery() != null) {
            existing.setEstimatedDelivery(dto.getEstimatedDelivery());
        }

        existing.setTrackingUpdated(new Date());
        existing.setTrackingLink(dto.getTrackingLink());
        existing.setTrackingRefNo(dto.getTrackingRefNo());
        existing.setDeliveryDate(dto.getDeliveryDate());


        if (dto.getTrackingStatus() != null) {
            try {
                Orders order = existing.getOrder();
                Payment payment = order.getPayment();
                AccountDetails account = payment.getAccountDetails();

                mailService.sendOrderStatusMail(
                        account.getAccountEmail(),
                        order.getOrderId(),
                        account.getFirstName(),
                        account.getLastName(),
                        TrackingStatus.valueOf(dto.getTrackingStatus().toString().toUpperCase()) // converts string to enum
                );

                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setTitle("Order Status Updated");
                notificationDTO.setMessage("Your order " + order.getId() + " is now " +
                        TrackingStatus.valueOf(dto.getTrackingStatus().toString().toUpperCase()));
                notificationDTO.setType("TRACKING_UPDATE");
                notificationDTO.setUserId(account.getUser().getId());

                messagingTemplate.convertAndSend("/queue/user/" + account.getUser().getId(), notificationDTO);

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send order status email: " + e.getMessage());
            }
        }

        OrdersTracking updatedTracking = ordersTrackingRepository.save(existing);
        return new AuthResponse(HttpStatus.OK.value(), "Tracking updated successfully", updatedTracking);
    }



}
