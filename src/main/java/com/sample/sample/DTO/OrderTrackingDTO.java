package com.sample.sample.DTO;

import com.sample.sample.Model.TrackingStatus;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDateTime;
import java.util.Date;

public class OrderTrackingDTO {
    private TrackingStatus trackingStatus;
    private Boolean shipped;
    private Boolean packed;
    private Boolean outOfDelivery;
    private Boolean delivered;
    private String trackingLink;
    private String trackingRefNo;

    @Temporal(TemporalType.DATE)
    private Date deliveryDate;

    private LocalDateTime deliveryTime;

    @Temporal(TemporalType.DATE)
    private Date estimatedDelivery;

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public String getTrackingRefNo() {
        return trackingRefNo;
    }

    public void setTrackingRefNo(String trackingRefNo) {
        this.trackingRefNo = trackingRefNo;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(Date estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public TrackingStatus getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(TrackingStatus trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public Boolean getPacked() {
        return packed;
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
}
