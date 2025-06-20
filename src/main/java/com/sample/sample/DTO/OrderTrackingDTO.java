package com.sample.sample.DTO;

import com.sample.sample.Model.TrackingStatus;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

public class OrderTrackingDTO {
    private TrackingStatus trackingStatus;
    private Boolean shipped;
    private Boolean packed;
    private Boolean outOfDelivery;
    private Boolean delivered;

    @Temporal(TemporalType.DATE)
    private Date estimatedDelivery;

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
