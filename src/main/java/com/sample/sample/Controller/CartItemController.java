package com.sample.sample.Controller;


import com.sample.sample.Model.CartItem;
import com.sample.sample.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    public CartItemService getCartItemService(CartItemService cartItemService) {
        return this.cartItemService =  cartItemService;
    }



    @PostMapping(value = "/add/{userId}/{productId}", consumes = {"multipart/form-data"})
    public ResponseEntity<CartItem> addCartItem(
            @PathVariable Long productId,
            @PathVariable String userId,
            @RequestParam("cartPayload") String cartItem,
            @RequestParam(defaultValue = "customImages", required = false) List<MultipartFile> customImages) throws IOException {

        CartItem savedCartItem = cartItemService.addCartItem(productId,userId, cartItem, customImages);
        return ResponseEntity.ok(savedCartItem);
    }




}
