package com.sample.sample.Controller;

import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestParam String userId, @RequestParam Long productId) {
        AuthResponse wishlist = wishlistService.addToWishlist(userId, productId);
        return ResponseEntity.status(wishlist.getCode()).body(wishlist);
    }

    // Delete single wishlist item
    @DeleteMapping("/delete/{wishlistId}/{userId}")
    public ResponseEntity<?> deleteWishlistItem( @PathVariable Long wishlistId,@PathVariable String userId) {
        AuthResponse response = wishlistService.deleteWishListItem(wishlistId, userId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // Delete all wishlist items for user
    @DeleteMapping("/deleteAll/{userId}")
    public ResponseEntity<?> deleteAllWishlist(@PathVariable String userId) {
       AuthResponse response=  wishlistService.deleteAllWishlistItems(userId);
        return ResponseEntity.status(response.getCode()).body(response);
    }


    @GetMapping("/items/{userId}")
    public ResponseEntity<?> getWishlist(@PathVariable String userId) {
        AuthResponse response = wishlistService.getUserWishlistItems(userId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}
