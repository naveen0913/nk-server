package com.sample.sample.Model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    private String cartItemName;

    private int cartQuantity;

    private boolean cartGiftWrap;

    private double totalPrice;

    private String customName;

    private Long optionCount;

    private double optionPrice;

    private double optiondiscount;

    private double optiondiscountPrice;

    @ElementCollection
    private List<String> customImages = new ArrayList<>();

    @ElementCollection
    @MapKeyColumn(name = "label_name")
    @Column(name = "label_value")
    private Map<String, Boolean> labelDesigns = new HashMap<>();

    @ManyToOne(optional = false) // Mandatory
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false) // Mandatory
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    @JsonBackReference
    private Payment payment;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }


    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getCartItemName() {
        return cartItemName;
    }

    public void setCartItemName(String cartItemName) {
        this.cartItemName = cartItemName;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public boolean isCartGiftWrap() {
        return cartGiftWrap;
    }

    public void setCartGiftWrap(boolean cartGiftWrap) {
        this.cartGiftWrap = cartGiftWrap;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Long getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(Long optionCount) {
        this.optionCount = optionCount;
    }

    public double getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(double optionPrice) {
        this.optionPrice = optionPrice;
    }

    public double getOptiondiscount() {
        return optiondiscount;
    }

    public void setOptiondiscount(double optiondiscount) {
        this.optiondiscount = optiondiscount;
    }

    public double getOptiondiscountPrice() {
        return optiondiscountPrice;
    }

    public void setOptiondiscountPrice(double optiondiscountPrice) {
        this.optiondiscountPrice = optiondiscountPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getCustomImages() {
        return customImages;
    }

    public void setCustomImages(List<String> customImages) {
        this.customImages = customImages;
    }

    public Map<String, Boolean> getLabelDesigns() {
        return labelDesigns;
    }

    public void setLabelDesigns(Map<String, Boolean> labelDesigns) {
        this.labelDesigns = labelDesigns;
    }
}
