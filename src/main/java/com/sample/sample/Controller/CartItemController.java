package com.sample.sample.Controller;

import com.sample.sample.Responses.AuthResponse;
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
@CrossOrigin("*")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    public CartItemService getCartItemService(CartItemService cartItemService) {
        return this.cartItemService =  cartItemService;
    }

    @PostMapping(value = "/add/{option}/{userId}/{productId}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addCartItem(
            @PathVariable Long option,
            @PathVariable Long productId,
            @PathVariable String userId,
            @RequestParam("cartPayload") String cartItem,
            @RequestParam(defaultValue = "customImages", required = false) List<MultipartFile> customImages) throws IOException {

        AuthResponse response = cartItemService.addCartItem(option,productId,userId, cartItem, customImages);
        return ResponseEntity.status(response.getCode()).body(response);
    }


    @PutMapping(value = "/update/{cartItemId}", consumes = {"multipart/form-data"})
    public AuthResponse updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam("cartPayload") String cartPayload,
            @RequestParam(value = "customImages", required = false) List<MultipartFile> customImages) throws IOException {

        cartItemService.updateCartItemById(cartItemId, cartPayload, customImages);
        return new AuthResponse(HttpStatus.OK.value(), "Cart Item Updated", null);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AuthResponse> getUserCartItems(@PathVariable String userId) {
        AuthResponse response = cartItemService.getUserCartList(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/all")
    public ResponseEntity<AuthResponse> getAllItems() {
        AuthResponse response = cartItemService.getAllCartItems();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/users")
    public ResponseEntity<AuthResponse> getAllUsers() {
        AuthResponse response = cartItemService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }





    @DeleteMapping("/{id}")
    public AuthResponse deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return new AuthResponse(HttpStatus.OK.value(), "deleted",null);
    }


    @DeleteMapping("/delete-all/{userId}")
    public ResponseEntity<AuthResponse> deleteAllCartItems(@PathVariable String userId) {
        AuthResponse response = cartItemService.deleteAllCartItems(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


}
