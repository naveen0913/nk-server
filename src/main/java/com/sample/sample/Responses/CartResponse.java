package com.sample.sample.Responses;

import java.util.List;

public class CartResponse {
    private Long cartId;
    private String userId;
    private String sessionId;
    private String status;
    private List<CartItemResponse> items;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CartResponse(Long cartId, String userId, String sessionId, String status, List<CartItemResponse> items) {
        this.cartId = cartId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.status = status;
        this.items = items;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
