package com.sample.sample.DTO;

import java.util.List;

public class ProductCustomizationDTO {

    private String description;
    private boolean input;
    private boolean quantity;
    private boolean cart;
    private boolean multiUpload;
    private boolean upload;
    private boolean design;
    private boolean giftWrap;
    private List<CustomizationOptionDTO> options;

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public boolean isQuantity() {
        return quantity;
    }

    public void setQuantity(boolean quantity) {
        this.quantity = quantity;
    }

    public boolean isCart() {
        return cart;
    }

    public void setCart(boolean cart) {
        this.cart = cart;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public boolean isDesign() {
        return design;
    }

    public void setDesign(boolean design) {
        this.design = design;
    }

    public boolean isGiftWrap() {
        return giftWrap;
    }

    public void setGiftWrap(boolean giftWrap) {
        this.giftWrap = giftWrap;
    }

    public boolean isMultiUpload() {
        return multiUpload;
    }

    public void setMultiUpload(boolean multiUpload) {
        this.multiUpload = multiUpload;
    }

    public List<CustomizationOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<CustomizationOptionDTO> options) {
        this.options = options;
}
}