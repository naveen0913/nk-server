package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private Boolean shipped;
    private Boolean packed;
    private Boolean outOfDelivery;
    private Boolean delivered;


    public Boolean getPacked() {
        return packed;
    }

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date trackingUpdated;

    @Temporal(TemporalType.DATE)
    private Date estimatedDelivery;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    @JsonBackReference
    @JsonIgnore
    private Orders order;


    private String trackingRefNo;
    private String trackingLink;
    private Date deliveryDate;

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

    public Boolean getShipped() {
        return shipped;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public void setPacked(Boolean packed) {
        this.packed = packed;
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

    public Date getTrackingUpdated() {
        return trackingUpdated;
    }

    public void setTrackingUpdated(Date trackingUpdated) {
        this.trackingUpdated = trackingUpdated;
    }

    public Date getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(Date estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public String getTrackingRefNo() {
        return trackingRefNo;
    }

    public void setTrackingRefNo(String trackingRefNo) {
        this.trackingRefNo = trackingRefNo;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
