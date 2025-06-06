package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class ProductCustomization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bannerImageUrl;

    @ElementCollection
    private List<String> thumbnailImageUrls;

    private boolean input;
    private boolean quantity;
    private boolean cart;
    private boolean upload;
    private boolean design;
    private boolean giftWrap;

    @OneToMany(mappedBy = "productCustomization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CustomizationOption> options;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public List<String> getThumbnailImageUrls() {
        return thumbnailImageUrls;
    }

    public void setThumbnailImageUrls(List<String> thumbnailImageUrls) {
        this.thumbnailImageUrls = thumbnailImageUrls;
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

    public List<CustomizationOption> getOptions() {
        return options;
    }

    public void setOptions(List<CustomizationOption> options) {
        this.options = options;
    }
}
