package com.sample.sample.Responses;

import java.util.List;

public class PaymentResponse {
    private Long id;
    private String razorpayOrderId;
    private String paymentId;
    private String signature;
    private Integer amount;
    private Integer gstAmount;
    private Integer shippingPrice;
    private String currency;
    private String receipt;
    private String status;
    private AccountDetailsResponse accountDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AccountDetailsResponse getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetailsResponse accountDetails) {
        this.accountDetails = accountDetails;
    }
}


