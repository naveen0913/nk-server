package com.sample.sample.Responses;

import com.sample.sample.Repository.productStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ImageResponse {
    private Long productId;
    private String productName;
    private String productdescription;
    private boolean productOrdered;
    private productStatus status;
    private String customProductId;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String pCategory;
    private String pSubcategory;
    private boolean inStock;
    private Long totalQuantity;
    private Long availableQuantity;
    private String pTag;
    private Double price;
    private Double discountPrice;
    private String weight;
    private String weightUnit;
    private String attributeName;
    private String attributeValue;
    private List<ProductImagesResponse> images;
    private boolean wishlisted;

    public List<ProductImagesResponse> getImages() {
        return images;
    }

    public void setImages(List<ProductImagesResponse> images) {
        this.images = images;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getpCategory() {
        return pCategory;
    }

    public void setpCategory(String pCategory) {
        this.pCategory = pCategory;
    }

    public productStatus getStatus() {
        return status;
    }

    public void setStatus(productStatus status) {
        this.status = status;
    }

    public boolean isProductOrdered() {
        return productOrdered;
    }

    public void setProductOrdered(boolean productOrdered) {
        this.productOrdered = productOrdered;
    }

    public String getCustomProductId() {
        return customProductId;
    }

    public void setCustomProductId(String customProductId) {
        this.customProductId = customProductId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getpSubcategory() {
        return pSubcategory;
    }

    public void setpSubcategory(String pSubcategory) {
        this.pSubcategory = pSubcategory;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getpTag() {
        return pTag;
    }

    public void setpTag(String pTag) {
        this.pTag = pTag;
    }

    public ImageResponse(Long productId, String productName, String productdescription, boolean productOrdered, productStatus status, String customProductId, LocalDateTime created, LocalDateTime updated, String pCategory, String pSubcategory, boolean inStock, Long totalQuantity, Long availableQuantity, String pTag, Double price, Double discountPrice, String weight, String weightUnit, String attributeName, String attributeValue, List<ProductImagesResponse> images, boolean wishlisted) {
        this.productId = productId;
        this.productName = productName;
        this.productdescription = productdescription;
        this.productOrdered = productOrdered;
        this.status = status;
        this.customProductId = customProductId;
        this.created = created;
        this.updated = updated;
        this.pCategory = pCategory;
        this.pSubcategory = pSubcategory;
        this.inStock = inStock;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.pTag = pTag;
        this.price = price;
        this.discountPrice = discountPrice;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.images = images;
        this.wishlisted = wishlisted;
    }

    public static class ProductImagesResponse {
        private Long imageId;
        private String imageUrl;
        private Long productId;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Long getImageId() {
            return imageId;
        }

        public void setImageId(Long imageId) {
            this.imageId = imageId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public ProductImagesResponse(Long imageId, String imageUrl, Long productId) {
            this.imageId = imageId;
            this.imageUrl = imageUrl;
            this.productId = productId;
        }
    }

    public boolean isWishlisted() {
        return wishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        this.wishlisted = wishlisted;
    }
}
