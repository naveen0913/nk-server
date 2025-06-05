package com.sample.sample.Responses;

public class ImageResponse {
    private Long imgId;

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    private String name;
    private String description;
    private String imageUrl;



    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ImageResponse(Long imgId, String name, String description, String imageUrl) {
        this.imgId = imgId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
