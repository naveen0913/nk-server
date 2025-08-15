package com.sample.sample.Model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
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
    @MapKeyColumn(name = "design_id")
    @Column(name = "design")
    private Map<String, Long> designs = new HashMap<>();

//    @Lob
//    @Column(name = "designs", columnDefinition = "TEXT")
//    private String designs;


    @ManyToOne(optional = false) // Mandatory
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false) // Mandatory
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_item_id", nullable = false) // New FK name
    private List<CustomizationOption> customizationOption = new ArrayList<>();

    public List<CustomizationOption> getCustomizationOption() {
        return customizationOption;
    }

    public void setCustomizationOption(List<CustomizationOption> customizationOption) {
        this.customizationOption = customizationOption;
    }

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public Map<String, Long> getDesigns() {
        return designs;
    }

    public void setDesigns(Map<String, Long> designs) {
        this.designs = designs;
    }


}
