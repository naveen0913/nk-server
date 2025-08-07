package com.sample.sample.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class DesignImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designImageId;

    private String designUrl;

    @ManyToOne
    @JoinColumn(name = "design_id")
    @JsonIgnore
    private Design design;

    // Getters and Setters
    public Long getDesignImageId() {
        return designImageId;
    }

    public void setDesignImageId(Long designImageId) {
        this.designImageId = designImageId;
    }

    public String getDesignUrl() {
        return designUrl;
    }

    public void setDesignUrl(String designUrl) {
        this.designUrl = designUrl;
    }

    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }
}
