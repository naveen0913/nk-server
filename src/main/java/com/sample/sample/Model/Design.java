package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designId;

    private String designName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Products product;

    @OneToMany(mappedBy = "design", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DesignImage> designImages = new ArrayList<>();


    public Long getDesignId() {
        return designId;
    }

    public void setDesignId(Long designId) {
        this.designId = designId;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public List<DesignImage> getDesignImages() {
        return designImages;
    }

    public void setDesignImages(List<DesignImage> designImages) {
        this.designImages = designImages;
    }
}
