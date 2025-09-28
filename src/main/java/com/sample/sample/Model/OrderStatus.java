package com.sample.sample.Model;


public enum OrderStatus {
    CREATED,          // Order created from cart, not yet paid
    PAYMENT_PENDING,  // waiting for payment (online) or COD pending
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

