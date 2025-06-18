package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String orderStatus;

    private Integer orderTotal;

    private String orderDiscount;

    private String orderGstPercent;

    private String orderShippingCharges;

//    @OneToOne
//    @JoinColumn(name = "account_id")
//    private AccountDetails accountDetails;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Transient
    public AccountDetails getAccountDetails() {
        return this.payment != null ? this.payment.getAccountDetails() : null;
    }

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


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
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

    public String getOrderShippingCharges() {
        return orderShippingCharges;
    }

    public void setOrderShippingCharges(String orderShippingCharges) {
        this.orderShippingCharges = orderShippingCharges;
    }

//    public AccountDetails getAccountDetails() {
//        return accountDetails;
//    }
//
//    public void setAccountDetails(AccountDetails accountDetails) {
//        this.accountDetails = accountDetails;
//    }

//    public UserAddress getUserAddress() {
//        return userAddress;
//    }
//
//    public void setUserAddress(UserAddress userAddress) {
//        this.userAddress = userAddress;
//    }

//    public List<CartItem> getCartItemList() {
//        return cartItemList;
//    }
//
//    public void setCartItemList(List<CartItem> cartItemList) {
//        this.cartItemList = cartItemList;
//    }

    public String getOrderGstPercent() {
        return orderGstPercent;
    }

    public void setOrderGstPercent(String orderGstPercent) {
        this.orderGstPercent = orderGstPercent;
    }

}
