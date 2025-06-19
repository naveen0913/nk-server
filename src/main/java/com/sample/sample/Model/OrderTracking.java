package com.example.demo.model;

import com.sample.sample.Model.Orders;
import jakarta.persistence.*;

@Entity
public class OrderTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackingId;

    private String trackingStatus;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    private Boolean isShipped = false;
    private Boolean isPacked = false;
    private Boolean outOfDelivery = false;
    private Boolean delivered = false;



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Boolean getShipped() {
        return isShipped;
    }

    public void setShipped(Boolean shipped) {
        isShipped = shipped;
    }

    public Boolean getPacked() {
        return isPacked;
    }

    public void setPacked(Boolean packed) {
        isPacked = packed;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Boolean getIsShipped() {
        return isShipped;
    }

    public void setIsShipped(Boolean isShipped) {
        this.isShipped = isShipped;
    }

    public Boolean getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(Boolean isPacked) {
        this.isPacked = isPacked;
    }

    public Boolean getOutOfDelivery() {
        return outOfDelivery;
    }

    public void setOutOfDelivery(Boolean outOfDelivery) {
        this.outOfDelivery = outOfDelivery;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }
}
