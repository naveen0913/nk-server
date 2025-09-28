package com.sample.sample.Responses;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.MapKeyColumn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartItemResponse {
    private Long cartItemId;
    private int cartQuantity;
    private ImageResponse product;

    public CartItemResponse(Long cartItemId, int cartQuantity, ImageResponse product) {
        this.cartItemId = cartItemId;
        this.cartQuantity = cartQuantity;
        this.product = product;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public ImageResponse getProduct() {
        return product;
    }

    public void setProduct(ImageResponse product) {
        this.product = product;
    }
}
