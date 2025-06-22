package com.sample.sample.DTO;

public class NotificationDTO {
//    private String title;
//    private String message;
//    private String recipientType;
//    private String orderId;

    private String title;
    private String message;
    private String type; // e.g., "TRACKING_UPDATE"
    private String userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
