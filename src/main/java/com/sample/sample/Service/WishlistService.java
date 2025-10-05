package com.sample.sample.Service;

import com.sample.sample.Model.Products;
import com.sample.sample.Model.User;
import com.sample.sample.Model.Wishlist;
import com.sample.sample.Repository.ProductsRepository;
import com.sample.sample.Repository.UserRepo;
import com.sample.sample.Repository.WishlistRepo;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.ImageResponse;
import com.sample.sample.Responses.WishlistResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductsRepository productRepo;

    public AuthResponse addToWishlist(String userId, Long productId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Products product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        boolean exists = wishlistRepo.findByUserAndProduct(user, product).isPresent();
        if (exists) {
            return new AuthResponse(HttpStatus.CONFLICT.value(), "Product already existed", null);
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        product.setWishlisted(true);
        wishlist.setProduct(product);
        wishlistRepo.save(wishlist);
        return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
    }

    public AuthResponse getUserWishlistItems(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Wishlist> wishlistItems = wishlistRepo.findByUser(user);

        // Map Wishlist entities to WishlistResponse DTOs
        List<WishlistResponse> wishlistResponses = wishlistItems.stream().map(wishlist -> {
            Products product = wishlist.getProduct();
            List<ImageResponse.ProductImagesResponse> imageResponses = product.getProductImages()
                    .stream()
                    .map(image -> new ImageResponse.ProductImagesResponse(
                            image.getImageId(),
                            image.getImageUrl(),
                            product.getProductId()
                    ))
                    .collect(Collectors.toList());

            ImageResponse imageResponse = new ImageResponse(
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductDescription(),
                    product.isProductOrdered(),
                    product.getProductStatus(),
                    product.getCustomProductId(),
                    product.getCreatedTime(),
                    product.getUpdatedTime(),
                    product.getpCategory(),
                    product.getpSubCategory(),
                    product.isInStock(),
                    product.getTotalQuantity(),
                    product.getAvailableQuantity(),
                    product.getpTag(),
                    product.getPrice(),
                    product.getDiscountPrice(),
                    product.getWeight(),
                    product.getWeightUnit(),
                    product.getAttributeName(),
                    product.getAttributeValue(),
                    imageResponses,
                    product.isWishlisted()
            );

            return new WishlistResponse(
                    wishlist.getWishlistId(),
                    imageResponse
            );
        }).collect(Collectors.toList());

        return new AuthResponse(HttpStatus.OK.value(), "Success", wishlistResponses);
    }


    @Transactional
    public AuthResponse deleteWishListItem(Long wishlistId,String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Wishlist wishlist = wishlistRepo.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist item not found"));
        wishlist.getProduct().setWishlisted(false);
        wishlistRepo.delete(wishlist);
        return new AuthResponse(HttpStatus.OK.value(), "deleted", null);
    }

    @Transactional
    public AuthResponse removeWishlistByProduct(Long pId, String userId) {
        // 1. Check if user exists
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 2. Check if product exists
        Products product = productRepo.findById(pId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // 3. Find the wishlist item for this user and product
        Wishlist wishlist = wishlistRepo.findByUserAndProduct(user, product)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist item not found for this product"));

        product.setWishlisted(false);

        // 5. Delete the wishlist item
        wishlistRepo.delete(wishlist);

        return new AuthResponse(HttpStatus.OK.value(), "Wishlist item deleted successfully", null);
    }


    @Transactional
    public AuthResponse deleteAllWishlistItems(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        wishlistRepo.deleteByUser(user);
        return new AuthResponse(HttpStatus.OK.value(), "deleted", null);

    }


}
