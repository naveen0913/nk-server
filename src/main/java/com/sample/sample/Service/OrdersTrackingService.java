package com.sample.sample.Service;

import com.sample.sample.DTO.NotificationDTO;
import com.sample.sample.DTO.OrderTrackingDTO;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.OrderRepository;
import com.sample.sample.Repository.OrdersTrackingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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


    public Optional<OrdersTracking> getOrderTrackingById(Long orderId){
        Orders existedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found with ID: " + orderId));
        Optional<OrdersTracking> ordersTrackingList = ordersTrackingRepository.findById(existedOrder.getId());
        return ordersTrackingList;
    }

    public List<OrdersTracking> getAllOrdersTracking(){
        return ordersTrackingRepository.findAll();
    }

    public Optional<OrdersTracking> getTrackingById(Long trackingId){
        Optional<OrdersTracking> existedTracking = ordersTrackingRepository.findById(trackingId);
        if (!existedTracking.isPresent()){
            throw new EntityNotFoundException("Order Tracking not found");
        }
        return existedTracking;

    }

    @Transactional
    public OrdersTracking updateTracking(Long trackingId, OrderTrackingDTO dto) {
        OrdersTracking existing = ordersTrackingRepository.findById(trackingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tracking not found with ID: " + trackingId));

        // Update tracking fields if present
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

        // Send mail only if trackingStatus is provided
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
                notificationDTO.setMessage("Your order " + order.getId() + " is now " + TrackingStatus.valueOf(dto.getTrackingStatus().toString().toUpperCase()));
                notificationDTO.setType("TRACKING_UPDATE");
                notificationDTO.setUserId(account.getUser().getId());
                messagingTemplate.convertAndSend("/queue/user/" + account.getUser().getId(), notificationDTO);

//                notificationService.notifyUser(account.getUser().getId(), notificationDTO);

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send order status email: " + e.getMessage());
            }
        }

        return ordersTrackingRepository.save(existing);
    }



}
