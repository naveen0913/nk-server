package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_number")
    private String orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String orderStatus;

    private Integer orderTotal;

    private String orderDiscount;

    private String orderGstPercent;

    private String orderShippingCharges;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrdersTracking orderTracking;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOrderedItems> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private UserAddress userAddress;

    @Transient
    public AccountDetails getAccountDetails() {
        return this.payment != null ? this.payment.getAccountDetails() : null;
    }

    public List<UserOrderedItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<UserOrderedItems> orderItems) {
        this.orderItems = orderItems;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
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

    public OrdersTracking getOrderTracking() {
        return orderTracking;
    }

    public void setOrderTracking(OrdersTracking orderTracking) {
        this.orderTracking = orderTracking;
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
