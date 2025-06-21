package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "product_customization_thumbnail_urls")
public class CustomizationThumbnailUrls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long thumbnailId;

    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "product_customization_id",nullable = false)
    @JsonBackReference
    private ProductCustomization productCustomization;

    public Long getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public ProductCustomization getProductCustomization() {
        return productCustomization;
    }

    public void setProductCustomization(ProductCustomization productCustomization) {
        this.productCustomization = productCustomization;
    }
}
