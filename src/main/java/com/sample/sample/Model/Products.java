package com.sample.sample.Model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sample.sample.Repository.productStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private String productDescription;
    private String productUrl;
    private boolean productOrdered;

    private String productShapeType;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Design> designs = new ArrayList<>();

    public List<Design> getDesigns() {
        return designs;
    }

    public void setDesigns(List<Design> designs) {
        this.designs = designs;
    }

    @Column(updatable = false, unique = true)
    private String customProductId;


    @Enumerated(EnumType.STRING)
    private productStatus productStatus;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    // Getters and Setters
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }





    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public productStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(productStatus productStatus) {
        this.productStatus = productStatus;
    }

    public String getCustomProductId() {
        return customProductId;
    }

    public void setCustomProductId(String customProductId) {
        this.customProductId = customProductId;
    }

    public boolean isProductOrdered() {
        return productOrdered;
    }

    public void setProductOrdered(boolean productOrdered) {
        this.productOrdered = productOrdered;
    }

    public ProductCustomization getProductCustomization() {
        return productCustomization;
    }

    public void setProductCustomization(ProductCustomization productCustomization) {
        this.productCustomization = productCustomization;
    }

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    @JsonBackReference
    private ProductCustomization productCustomization;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCustomizationImage> images = new ArrayList<>();

    public List<ProductCustomizationImage> getImages() {
        return images;
    }

    public void setImages(List<ProductCustomizationImage> images) {
        this.images = images;
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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductShapeType() {
        return productShapeType;
    }

    public void setProductShapeType(String productShapeType) {
        this.productShapeType = productShapeType;
    }
}

