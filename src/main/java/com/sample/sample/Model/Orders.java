package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal orderTotal;

    private String orderNote;

    @ManyToOne
    private User user;

    private String sessionId;

    private String orderShippingCharges;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrdersTracking orderTracking;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOrderedItems> orderItems;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Delivery delivery;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

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

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
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


    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getOrderShippingCharges() {
        return orderShippingCharges;
    }

    public void setOrderShippingCharges(String orderShippingCharges) {
        this.orderShippingCharges = orderShippingCharges;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

}
