package com.sample.sample.Responses;


import java.util.List;
import java.util.Map;

public class UserOrdereditemsResponse {
    private Long id;
    private int quantity;
    private double totalPrice;
    private ImageResponse product;


    public UserOrdereditemsResponse() {
    }

    public UserOrdereditemsResponse(Long id, int quantity, double totalPrice, ImageResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ImageResponse getProduct() {
        return product;
    }

    public void setProduct(ImageResponse product) {
        this.product = product;
    }
}
