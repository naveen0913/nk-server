package com.sample.sample.Model;

import jakarta.persistence.*;

@Entity
public class CustomerPreviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Hotspot hotspot;

    private Long productId;

    private String finalImageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotspot getHotspot() {
        return hotspot;
    }

    public void setHotspot(Hotspot hotspot) {
        this.hotspot = hotspot;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getFinalImageUrl() {
        return finalImageUrl;
    }

    public void setFinalImageUrl(String finalImageUrl) {
        this.finalImageUrl = finalImageUrl;
    }

}
