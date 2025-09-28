package com.sample.sample.DTO;

import com.sample.sample.Model.Payment;

import java.util.List;

public class OrderDTO {

    private String userId;
    private String sessionId;
    private Long shippingAddress;
    private String paymentMethod;

//    user requirements
    private String orderNote;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public Long getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Long shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
//    public void setOrderTotal(Integer orderTotal) {
//        this.orderTotal = orderTotal;
//    }
//
//    public String getOrderDiscount() {
//        return orderDiscount;
//    }
//
//    public void setOrderDiscount(String orderDiscount) {
//        this.orderDiscount = orderDiscount;
//    }
//
//    public List<Long> getCartItemIds() {
//        return cartItemIds;
//    }
//
//    public void setCartItemIds(List<Long> cartItemIds) {
//        this.cartItemIds = cartItemIds;
//    }
//
//    public String getShippingCharges() {
//        return shippingCharges;
//    }
//
//    public void setShippingCharges(String shippingCharges) {
//        this.shippingCharges = shippingCharges;
//    }


}
