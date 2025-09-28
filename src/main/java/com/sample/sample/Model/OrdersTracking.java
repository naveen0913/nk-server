package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "orders_tracking")
@EntityListeners(AuditingEntityListener.class)
public class OrdersTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date trackingCreated;

    private String trackingId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TrackingStatus trackingStatus;

    private Boolean delivered;

    private String note;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date trackingUpdated;


    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    @JsonBackReference
    @JsonIgnore
    private Orders order;


    private LocalDateTime deliveryTime;

    // --- Getters and Setters ---
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

    public TrackingStatus getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(TrackingStatus trackingStatus) {
        this.trackingStatus = trackingStatus;
    }


    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Date getTrackingUpdated() {
        return trackingUpdated;
    }

    public void setTrackingUpdated(Date trackingUpdated) {
        this.trackingUpdated = trackingUpdated;
    }


    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
