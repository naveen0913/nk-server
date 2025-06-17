package com.sample.sample.DTO;

import com.sample.sample.Model.Payment;

import java.util.List;

public class OrderDTO {


    private Integer orderTotal;
    private String orderDiscount;
    private List<Long> cartItemIds;
    private String shippingCharges;
    private String orderGstPercent;

    public String getOrderGstPercent() {
        return orderGstPercent;
    }

    public void setOrderGstPercent(String orderGstPercent) {
        this.orderGstPercent = orderGstPercent;
    }

    private Payment payment;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public void setCartItemIds(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public String getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(String shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

}
