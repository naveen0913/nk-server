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
    private String url;



    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }


    public ImageResponse(Long imgId, String name, String description, String url) {
        this.imgId = imgId;
        this.name = name;
        this.description = description;
        this.url = url;
    }
}
