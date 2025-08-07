package com.sample.sample.DTO;

import java.time.LocalDateTime;

public class CustomizationOptionDTO {

    private String optionLabel;
    private Double originalPrice;
    private Double oldPrice;
    private Double discount;
    private boolean mostPopular;
    private Integer optionSheetCount;

    // ✅ Add these two fields
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

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
}
