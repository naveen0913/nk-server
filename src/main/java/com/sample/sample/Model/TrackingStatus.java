package com.sample.sample.Model;

public enum TrackingStatus {
    CREATED,          // Order created from cart, not yet paid
    PAYMENT_PENDING,  // waiting for payment (online) or COD pending
    PAID,
    DELIVERED,
    CANCELLED
}
