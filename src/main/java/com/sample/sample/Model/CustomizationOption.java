package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class CustomizationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionLabel;
    private Double originalPrice;
    private Double oldPrice;
    private Double discount;
    private boolean mostPopular;

    private Integer optionSheetCount;

    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;
    @ManyToOne
    @JoinColumn(name = "product_customization_id", nullable = false)
    @JsonBackReference
    private ProductCustomization productCustomization;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public boolean isMostPopular() {
        return mostPopular;
    }

    public void setMostPopular(boolean mostPopular) {
        this.mostPopular = mostPopular;
    }

    public Integer getOptionSheetCount() {
        return optionSheetCount;
    }

    public void setOptionSheetCount(Integer optionSheetCount) {
        this.optionSheetCount = optionSheetCount;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public ProductCustomization getProductCustomization() {
        return productCustomization;
    }

    public void setProductCustomization(ProductCustomization productCustomization) {
        this.productCustomization = productCustomization;
    }
}
