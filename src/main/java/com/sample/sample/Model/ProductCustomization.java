package com.sample.sample.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductCustomization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String description;
    private boolean cart;
    private boolean input;
    private boolean quantity;
    private boolean upload;
    private boolean design;
    private boolean giftWrap;
    private boolean multiUpload;

    private String bannerImageUrl;

    @OneToOne(optional = true) // Product must exist
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    @JsonIgnore
    private Products product;

    @OneToMany(mappedBy = "productCustomization", cascade = CascadeType.ALL)
    private List<CustomizationThumbnailUrls> thumbnailImages = new ArrayList<>();

    @OneToMany(mappedBy = "productCustomization", cascade = CascadeType.ALL)
    private List<CustomizationOption> customizationOptions = new ArrayList<>();

    public List<CustomizationOption> getCustomizationOptions() {
        return customizationOptions;
    }

    public void setCustomizationOptions(List<CustomizationOption> customizationOptions) {
        this.customizationOptions = customizationOptions;
    }



    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isCart() {
        return cart;
    }

    public void setCart(boolean cart) {
        this.cart = cart;
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

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public List<CustomizationThumbnailUrls> getThumbnailImages() {
        return thumbnailImages;
    }

    public void setThumbnailImages(List<CustomizationThumbnailUrls> thumbnailImages) {
        this.thumbnailImages = thumbnailImages;
    }


}
