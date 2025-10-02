package com.sample.sample.Responses;

import java.time.LocalDateTime;

public class DeliveryResponse {
    private String trackingId;
    private String status;
    private double latitude;
    private double longitude;
    private String agentName;
    private String agentPhone;
    private String trackingWebSocketUrl;
    private Integer estimatedMinutes;
    private LocalDateTime assignedAt;
    private LocalDateTime deliveredAt;



    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    public String getTrackingWebSocketUrl() {
        return trackingWebSocketUrl;
    }

    public void setTrackingWebSocketUrl(String trackingWebSocketUrl) {
        this.trackingWebSocketUrl = trackingWebSocketUrl;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public DeliveryResponse() {
    }

    public DeliveryResponse(String trackingId, String status, double latitude, double longitude, String agentName, String agentPhone, String trackingWebSocketUrl, Integer estimatedMinutes, LocalDateTime assignedAt, LocalDateTime deliveredAt) {
        this.trackingId = trackingId;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.agentName = agentName;
        this.agentPhone = agentPhone;
        this.trackingWebSocketUrl = trackingWebSocketUrl;
        this.estimatedMinutes = estimatedMinutes;
        this.assignedAt = assignedAt;
        this.deliveredAt = deliveredAt;
    }
}
