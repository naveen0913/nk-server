package com.sample.sample.Model;

import jakarta.persistence.*;

@Entity
public class CartOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;
    private double optionPrice;
    private double oldPrice;
    private double optionDiscount;

    @ManyToOne
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public double getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(double optionPrice) {
        this.optionPrice = optionPrice;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public double getOptionDiscount() {
        return optionDiscount;
    }

    public void setOptionDiscount(double optionDiscount) {
        this.optionDiscount = optionDiscount;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}

