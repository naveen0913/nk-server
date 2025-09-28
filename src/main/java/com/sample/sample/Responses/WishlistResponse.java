package com.sample.sample.Responses;

import java.util.List;


public class WishlistResponse {
    private Long wishlistId;
    private ImageResponse product;



    public ImageResponse getProduct() {
        return product;
    }

    public void setProduct(ImageResponse product) {
        this.product = product;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public WishlistResponse(Long wishlistId, ImageResponse product) {
        this.wishlistId = wishlistId;
        this.product = product;
    }
}
