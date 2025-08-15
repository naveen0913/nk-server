package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductCustomizationImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customImage;

    @OneToMany(mappedBy = "productCustomizationImage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Hotspot> hotspots = new ArrayList<>();


    @OneToOne(optional = true)
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    @JsonIgnore
    private Products product;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomImage() {
        return customImage;
    }

    public void setCustomImage(String customImage) {
        this.customImage = customImage;
    }

    public List<Hotspot> getHotspots() {
        return hotspots;
    }

    public void setHotspots(List<Hotspot> hotspots) {
        this.hotspots = hotspots;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }



}
