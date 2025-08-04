package com.sample.sample.Responses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sample.sample.Model.ProductCustomization;

public class ImageResponse {
    private Long productId;
    private String productName;
    private String productdescription;
    private String productUrl;
    private boolean productOrdered;

    public boolean isProductOrdered() {
        return productOrdered;
    }

    public void setProductOrdered(boolean productOrdered) {
        this.productOrdered = productOrdered;
    }

    private ProductCustomization productCustomization;

    public ProductCustomization getProductCustomization() {
        return productCustomization;
    }

    public void setProductCustomization(ProductCustomization productCustomization) {
        this.productCustomization = productCustomization;
    }

    public ImageResponse(Long productId, String productName, String productdescription, String productUrl, boolean productOrdered, ProductCustomization productCustomization) {
        this.productId = productId;
        this.productName = productName;
        this.productdescription = productdescription;
        this.productUrl = productUrl;
        this.productOrdered = productOrdered;
        this.productCustomization = productCustomization;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
}
