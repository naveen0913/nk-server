package com.sample.sample.Responses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sample.sample.Model.Design;
import com.sample.sample.Model.ProductCustomization;
import com.sample.sample.Repository.productStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ImageResponse {
    private Long productId;
    private String productName;
    private String productdescription;
    private String productUrl;
    private boolean productOrdered;
    private productStatus status;
    private String customProductId;
    private LocalDateTime created;
    private LocalDateTime updated;
    private ProductCustomization productCustomization;
    private List<Design> productDesigns;
    private String productShapeType;

    public String getProductShapeType() {
        return productShapeType;
    }

    public void setProductShapeType(String productShapeType) {
        this.productShapeType = productShapeType;
    }

    public productStatus getStatus() {
        return status;
    }

    public void setStatus(productStatus status) {
        this.status = status;
    }

    public boolean isProductOrdered() {
        return productOrdered;
    }

    public void setProductOrdered(boolean productOrdered) {
        this.productOrdered = productOrdered;
    }



    public List<Design> getProductDesigns() {
        return productDesigns;
    }

    public void setProductDesigns(List<Design> productDesigns) {
        this.productDesigns = productDesigns;
    }

    public ProductCustomization getProductCustomization() {
        return productCustomization;
    }

    public void setProductCustomization(ProductCustomization productCustomization) {
        this.productCustomization = productCustomization;
    }

    public String getCustomProductId() {
        return customProductId;
    }

    public void setCustomProductId(String customProductId) {
        this.customProductId = customProductId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
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

    public ImageResponse(Long productId, String productName, String productdescription, String productUrl, boolean productOrdered, productStatus status, String customProductId, LocalDateTime created, LocalDateTime updated, ProductCustomization productCustomization, List<Design> productDesigns, String productShapeType) {
        this.productId = productId;
        this.productName = productName;
        this.productdescription = productdescription;
        this.productUrl = productUrl;
        this.productOrdered = productOrdered;
        this.status = status;
        this.customProductId = customProductId;
        this.created = created;
        this.updated = updated;
        this.productCustomization = productCustomization;
        this.productDesigns = productDesigns;
        this.productShapeType = productShapeType;
    }
}
