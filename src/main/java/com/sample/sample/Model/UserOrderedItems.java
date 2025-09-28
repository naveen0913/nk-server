package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "user_ordered_items")
public class UserOrderedItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int orderquantity;
    private double price;



    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderquantity() {
        return orderquantity;
    }

    public void setOrderquantity(int orderquantity) {
        this.orderquantity = orderquantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }
}
