package com.sample.sample.DTO;


import com.sample.sample.Model.PaymentStatus;

import java.util.List;

public class PaymentRequestDTO {
    private String razorpayOrderId;
    private String paymentId;
    private String signature;
    private List<Long> cartItemIds;
    private Integer amount;
    private Integer gstAmount;
    private Integer shippingPrice;
    private String currency;
    private String receipt;
    private PaymentStatus status;

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public void setCartItemIds(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(Integer gstAmount) {
        this.gstAmount = gstAmount;
    }

    public Integer getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Integer shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}

