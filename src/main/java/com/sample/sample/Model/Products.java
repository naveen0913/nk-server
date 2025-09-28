package com.sample.sample.Model;


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
    private boolean productOrdered;

    public List<ProductImages> getProductImages() {
        return productImages;
    }

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductImages> productImages = new ArrayList<>();

    @Column(updatable = false, unique = true)
    private String customProductId;
    private Double price;
    private Double discountPrice;
    @Enumerated(EnumType.STRING)
    private productStatus productStatus;

    private String pCategory;
    private String pSubCategory;
    private boolean inStock;
    private Long totalQuantity;
    private Long availableQuantity;
    private String pTag;
    private String weight;
    private String weightUnit;

    private String attributeName;
    private String attributeValue;

    @CreationTimestamp
    private LocalDateTime createdTime;

    private boolean wishlisted;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    // Getters and Setters
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setProductImages(List<ProductImages> productImages) {
        this.productImages = productImages;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }



    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getpSubCategory() {
        return pSubCategory;
    }

    public void setpSubCategory(String pSubCategory) {
        this.pSubCategory = pSubCategory;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getpTag() {
        return pTag;
    }

    public void setpTag(String pTag) {
        this.pTag = pTag;
    }

    public String getpCategory() {
        return pCategory;
    }

    public void setpCategory(String pCategory) {
        this.pCategory = pCategory;
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

    public boolean isWishlisted() {
        return wishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        this.wishlisted = wishlisted;
    }
}

