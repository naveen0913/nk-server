package com.sample.sample.Responses;

import com.sample.sample.Model.OrdersTracking;
import com.sample.sample.Model.UserOrderedItems;

import java.util.Date;
import java.util.List;

public class OrdersResponse {

        private Long id;
        private String orderId;
        private Date createdAt;
        private String orderStatus;
        private Integer orderTotal;
        private String orderDiscount;
        private String orderGstPercent;
        private String orderShippingCharges;

        private PaymentResponse payment;
        private OrdersTracking orderTracking;
        private List<UserOrderedItems> orderItems;
        private AccountDetailsResponse accountDetails;
        private UserAddressResponse userAddress;

    public UserAddressResponse getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddressResponse userAddress) {
        this.userAddress = userAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(String orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public String getOrderGstPercent() {
        return orderGstPercent;
    }

    public void setOrderGstPercent(String orderGstPercent) {
        this.orderGstPercent = orderGstPercent;
    }

    public String getOrderShippingCharges() {
        return orderShippingCharges;
    }

    public void setOrderShippingCharges(String orderShippingCharges) {
        this.orderShippingCharges = orderShippingCharges;
    }

    public PaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }

    public OrdersTracking getOrderTracking() {
        return orderTracking;
    }

    public void setOrderTracking(OrdersTracking orderTracking) {
        this.orderTracking = orderTracking;
    }

    public List<UserOrderedItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<UserOrderedItems> orderItems) {
        this.orderItems = orderItems;
    }

    public AccountDetailsResponse getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetailsResponse accountDetails) {
        this.accountDetails = accountDetails;
    }
}
