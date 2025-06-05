package com.sample.sample.DTO;



import com.sample.sample.Model.CustomizationOption;
import java.util.List;

public class ProductCustomizationDTO {
    private boolean input;
    private boolean quantity;
    private boolean cart;
    private boolean upload;
    private boolean design;
    private boolean giftWrap;
    private List<CustomizationOption> options;

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

    public List<CustomizationOption> getOptions() {
        return options;
    }

    public void setOptions(List<CustomizationOption> options) {
        this.options = options;
    }
}
