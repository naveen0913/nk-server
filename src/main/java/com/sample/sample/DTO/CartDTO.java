package com.sample.sample.DTO;

import java.util.List;
import java.util.Map;

public class CartDTO {
    private String cartItemName;

    private int cartQuantity;

    private boolean cartGiftWrap;

    private double totalPrice;

    private String customName;

    private Long optionCount;

    private double optionPrice;

    private double optiondiscount;

    private double optiondiscountPrice;

    private Map<String, Boolean> labelDesigns;

    public Map<String, Boolean> getLabelDesigns() {
        return labelDesigns;
    }

    public void setLabelDesigns(Map<String, Boolean> labelDesigns) {
        this.labelDesigns = labelDesigns;
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


}
