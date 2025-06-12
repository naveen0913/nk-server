package com.sample.sample.Model;





import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

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
    @ManyToOne
    @JoinColumn(name = "product_customization_id")
    @JsonBackReference
    private ProductCustomization productCustomization; // store ID directly

    // Getters and Setters

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

    public ProductCustomization getProductCustomization() {
        return productCustomization;
    }

    public void setProductCustomization(ProductCustomization productCustomization) {
        this.productCustomization = productCustomization;
    }

    public boolean isMostPopular() {
        return mostPopular;
    }

    public void setMostPopular(boolean mostPopular) {
        this.mostPopular = mostPopular;
}

}