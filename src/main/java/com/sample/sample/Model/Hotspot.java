package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Hotspot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shapeType;

    @Lob
    @Column(name = "coordinates", columnDefinition = "TEXT")
    private String coordinates;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_customization_image_id", nullable = false)
    private ProductCustomizationImage productCustomizationImage;



    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public ProductCustomizationImage getProductCustomizationImage() {
        return productCustomizationImage;
    }

    public void setProductCustomizationImage(ProductCustomizationImage productCustomizationImage) {
        this.productCustomizationImage = productCustomizationImage;
    }
}
