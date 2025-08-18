package com.sample.sample.Responses;

import java.util.List;

public class CustomImageResponse {
    private Long id;
    private String imageUrl;
    private List<List<CoordinateResponse>> hotspots;

    public CustomImageResponse() {
    }

    public CustomImageResponse(Long id, String imageUrl, List<List<CoordinateResponse>> hotspots) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.hotspots = hotspots;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<List<CoordinateResponse>> getHotspots() {
        return hotspots;
    }

    public void setHotspots(List<List<CoordinateResponse>> hotspots) {
        this.hotspots = hotspots;
    }

    public static class CoordinateResponse {
        private Long id;
        private double x;
        private double y;
        private String shape;

        public CoordinateResponse(){}

        public CoordinateResponse(Long id, double x, double y, String shape) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.shape = shape;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getShape() {
            return shape;
        }

        public void setShape(String shape) {
            this.shape = shape;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

}
