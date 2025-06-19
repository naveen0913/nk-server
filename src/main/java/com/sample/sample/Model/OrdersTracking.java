package com.sample.sample.Model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "orders_tracking")
public class OrdersTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date trackingCreated;

    private String trackingId;
    private String trackingStatus;
    private Boolean isShipped;
    private Boolean isPacked;
    private Boolean outOfDelivery;
    private Boolean delivered;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private Orders order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTrackingCreated() {
        return trackingCreated;
    }

    public void setTrackingCreated(Date trackingCreated) {
        this.trackingCreated = trackingCreated;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
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

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
}
