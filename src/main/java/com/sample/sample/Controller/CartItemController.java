package com.sample.sample.Controller;




import com.sample.sample.Model.CartItem;
import com.sample.sample.Model.User;
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

    @PostMapping(value = "/add/{userId}/{productId}", consumes = {"multipart/form-data"})
    public AuthResponse addCartItem(
            @PathVariable Long productId,
            @PathVariable String userId,
            @RequestParam("cartPayload") String cartItem,
            @RequestParam(defaultValue = "customImages", required = false) List<MultipartFile> customImages) throws IOException {

        cartItemService.addCartItem(productId,userId, cartItem, customImages);
        return new AuthResponse(HttpStatus.CREATED.value(),"created",null);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItem>> getUserCartItems(@PathVariable String userId) {
        List<CartItem> cartItems = cartItemService.getUserCartList(userId);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/all")
    public AuthResponse getAllItems() {
        List<CartItem> cartItems = cartItemService.getAllCartItems();
        return new AuthResponse(HttpStatus.OK.value(), "deleted",cartItems);
    }



    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = cartItemService.getAllUsers();
        return ResponseEntity.ok(users);
    }



    // Delete one cart item by ID
    @DeleteMapping("/{id}")
    public AuthResponse deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return new AuthResponse(HttpStatus.OK.value(), "deleted",null);
    }


    @DeleteMapping("/all")
    public AuthResponse deleteAllCartItems() {
        cartItemService.deleteAllCartItems();
        return new AuthResponse(HttpStatus.OK.value(), "deleted",null);
    }






    }





