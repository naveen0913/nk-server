package com.sample.sample.DTO;

public class OrderTrackingDTO {
    private String trackingStatus;
    private Boolean isShipped;
    private Boolean isPacked;
    private Boolean outOfDelivery;
    private Boolean delivered;

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
}
