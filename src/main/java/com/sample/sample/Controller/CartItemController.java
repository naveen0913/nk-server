package com.sample.sample.Controller;

import com.sample.sample.Model.Cart;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.CartResponse;
import com.sample.sample.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return this.cartItemService = cartItemService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "sessionId", required = false) String sessionId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            Cart cart = cartItemService.addItem(userId, sessionId, productId, quantity);
            return ResponseEntity.ok(
                    new AuthResponse(HttpStatus.CREATED.value(), "Item added to cart successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new AuthResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null)
            );
        }
    }

    @PostMapping("/merge/cart")
    public ResponseEntity<AuthResponse> mergeCart(
            @RequestParam String userId,
            @RequestParam String sessionId) {
        try {
            Cart mergedCart = cartItemService.mergeCart(userId, sessionId);
            return ResponseEntity.ok(
                    new AuthResponse(HttpStatus.OK.value(), "Guest cart merged successfully", mergedCart)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new AuthResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null)
            );
        }
    }

    @GetMapping("/items")
    public ResponseEntity<?> getCartItems(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String sessionId) {

        CartResponse cartResponse = cartItemService.getCart(userId, sessionId);

        return ResponseEntity.ok(
                new AuthResponse(
                        HttpStatus.OK.value(),
                        "Cart fetched successfully",
                        cartResponse
                )
        );
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCart(@RequestParam(required = false) String userId,
                                        @RequestParam(required = false) String sessionId,
                                        @RequestParam Long productId,
                                        @RequestParam int quantity) {
        AuthResponse response = cartItemService.updateCartItem(userId, sessionId, productId, quantity);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long cartId,
                                        @RequestParam(required = false) String userId,
                                        @RequestParam(required = false) String sessionId) {
        AuthResponse authResponse = cartItemService.deleteCartById(cartId,userId,sessionId);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

    @DeleteMapping("/delete/all/")
    public ResponseEntity<?> deleteAllItems(
                                        @RequestParam(required = false) String userId,
                                        @RequestParam(required = false) String sessionId) {
        AuthResponse authResponse = cartItemService.deleteAllCarts(userId,sessionId);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

}
