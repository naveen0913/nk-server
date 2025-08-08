package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "user_ordered_items")
public class UserOrderedItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int quantity;
    private boolean giftWrap;
    private double totalPrice;
    private String customName;
    private Long optionCount;
    private double optionPrice;
    private double optionDiscount;
    private double optionDiscountPrice;

    @ElementCollection
    private List<String> customImages = new ArrayList<>();

    @ElementCollection
    @MapKeyColumn(name = "ordered_design")
    @Column(name = "design_id")
    private Map<String, Long> designs = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "product_id")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isGiftWrap() {
        return giftWrap;
    }

    public void setGiftWrap(boolean giftWrap) {
        this.giftWrap = giftWrap;
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

    public double getOptionDiscount() {
        return optionDiscount;
    }

    public void setOptionDiscount(double optionDiscount) {
        this.optionDiscount = optionDiscount;
    }

    public double getOptionDiscountPrice() {
        return optionDiscountPrice;
    }

    public void setOptionDiscountPrice(double optionDiscountPrice) {
        this.optionDiscountPrice = optionDiscountPrice;
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
