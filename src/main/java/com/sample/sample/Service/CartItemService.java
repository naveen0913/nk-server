package com.sample.sample.Service;

import com.sample.sample.Model.*;
import com.sample.sample.Repository.*;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.CartItemResponse;
import com.sample.sample.Responses.CartResponse;
import com.sample.sample.Responses.ImageResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private MailService mailService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Cart addItem(String userId, String sessionId, Long productId, int quantity) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart;
        if (userId != null) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            cart = cartRepo.findByUserAndStatus(user, Cart.Status.ACTIVE)
                    .orElseGet(() -> {
                        Cart c = new Cart();
                        c.setUser(user);
                        c.setStatus(Cart.Status.ACTIVE);
                        return cartRepo.save(c);
                    });
        } else {

            if (sessionId == null) {
                throw new RuntimeException("Session ID required for guest cart");
            }
            cart = cartRepo.findBySessionIdAndStatus(sessionId, Cart.Status.ACTIVE)
                    .orElseGet(() -> {
                        Cart c = new Cart();
                        c.setSessionId(sessionId);
                        c.setStatus(Cart.Status.ACTIVE);
                        return cartRepo.save(c);
                    });
        }

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setCart(cart);
                    ci.setProduct(product);
                    ci.setCartQuantity(0);
                    return ci;
                });

        item.setCartQuantity(item.getCartQuantity() + quantity);
        cartItemRepository.save(item);
        return cart;
    }

//    public Cart mergeCart(String userId, String sessionId) {
//        // Find active guest cart
//        Optional<Cart> guestCartOpt = cartRepo.findBySessionIdAndStatus(sessionId, Cart.Status.ACTIVE);
//
//        if (guestCartOpt.isEmpty()) {
//            throw new RuntimeException("Guest cart not found");
//        }
//        Cart guestCart = guestCartOpt.get();
//        // Find or create user's active cart
//        User user = userRepo.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart userCart = cartRepo.findByUserAndStatus(user, Cart.Status.ACTIVE)
//                .orElseGet(() -> {
//                    Cart c = new Cart();
//                    c.setUser(user);
//                    c.setStatus(Cart.Status.ACTIVE);
//                    return cartRepo.save(c);
//                });
//        // Merge items
//        for (CartItem guestItem : guestCart.getItems()) {
//            CartItem existingItem = cartItemRepository.findByCartAndProduct(userCart, guestItem.getProduct())
//                    .orElseGet(() -> {
//                        CartItem ci = new CartItem();
//                        ci.setCart(userCart);
//                        ci.setProduct(guestItem.getProduct());
//                        ci.setCartQuantity(0);
//                        return ci;
//                    });
//
//            existingItem.setCartQuantity(existingItem.getCartQuantity() + guestItem.getCartQuantity());
//            cartItemRepository.save(existingItem);
//        }
//        // Mark guest cart as COMPLETED
//        guestCart.setStatus(Cart.Status.COMPLETED);
//        cartRepo.save(guestCart);
//        return userCart;
//    }

    @Transactional
    public Cart mergeCart(String userId, String sessionId) {
        Optional<Cart> guestCartOpt = cartRepo.findBySessionIdAndStatus(sessionId, Cart.Status.ACTIVE);

        if (guestCartOpt.isEmpty()) {
            return cartRepo.findByUserAndStatus(new User(userId), Cart.Status.ACTIVE)
                    .orElseThrow(() -> new RuntimeException("No cart found for user or session"));
        }

        Cart guestCart = guestCartOpt.get();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart userCart = cartRepo.findByUserAndStatus(user, Cart.Status.ACTIVE)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    c.setStatus(Cart.Status.ACTIVE);
                    return cartRepo.save(c);
                });

        for (CartItem guestItem : guestCart.getItems()) {
            CartItem existingItem = cartItemRepository.findByCartAndProduct(userCart, guestItem.getProduct())
                    .orElseGet(() -> {
                        CartItem ci = new CartItem();
                        ci.setCart(userCart);
                        ci.setProduct(guestItem.getProduct());
                        ci.setCartQuantity(0);
                        return ci;
                    });

            existingItem.setCartQuantity(existingItem.getCartQuantity() + guestItem.getCartQuantity());
            cartItemRepository.save(existingItem);
        }

        guestCart.setStatus(Cart.Status.COMPLETED);
        cartRepo.save(guestCart);
        return userCart;
    }

    public CartResponse getCart(String userId, String sessionId) {
        Cart cart = null;

        if (userId != null) {
            cart = cartRepo.findByUserAndStatus(new User(userId), Cart.Status.ACTIVE)
                    .orElse(null);

            // merge if session cart exists
            Optional<Cart> guestCartOpt = cartRepo.findBySessionIdAndStatus(sessionId, Cart.Status.ACTIVE);
            if (guestCartOpt.isPresent()) {
                cart = mergeCart(userId, sessionId);
            }
        } else if (sessionId != null) {
            cart = cartRepo.findBySessionIdAndStatus(sessionId, Cart.Status.ACTIVE).orElse(null);
        }

        if (cart == null) {
            return new CartResponse(null, userId, sessionId, "EMPTY", List.of());
        }

        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getCartItemId(),
                        item.getCartQuantity(),
                        new ImageResponse(
                                item.getProduct().getProductId(),
                                item.getProduct().getProductName(),
                                item.getProduct().getProductDescription(),
                                item.getProduct().isProductOrdered(),
                                item.getProduct().getProductStatus(),
                                item.getProduct().getCustomProductId(),
                                item.getProduct().getCreatedTime(),
                                item.getProduct().getUpdatedTime(),
                                item.getProduct().getpCategory(),
                                item.getProduct().getpSubCategory(),
                                item.getProduct().isInStock(),
                                item.getProduct().getTotalQuantity(),
                                item.getProduct().getAvailableQuantity(),
                                item.getProduct().getpTag(),
                                item.getProduct().getPrice(),
                                item.getProduct().getDiscountPrice(),
                                item.getProduct().getWeight(),
                                item.getProduct().getWeightUnit(),
                                item.getProduct().getAttributeName(),
                                item.getProduct().getAttributeValue(),
                                productImageResponse(item.getProduct()),
                                item.getProduct().isWishlisted()
                        )
                ))
                .toList();

        String userIdFromCart = (cart.getUser() != null) ? cart.getUser().getId() : null;

        return new CartResponse(
                cart.getCartId(),
                userIdFromCart,
                cart.getSessionId(),
                cart.getStatus().name(),
                itemResponses
        );
    }

    private List<ImageResponse.ProductImagesResponse> productImageResponse(Products product) {
        List<ImageResponse.ProductImagesResponse> imageResponses = product.getProductImages()
                .stream()
                .map(image -> new ImageResponse.ProductImagesResponse(
                        image.getImageId(),
                        image.getImageUrl(),
                        product.getProductId()
                ))
                .collect(Collectors.toList());
        return imageResponses;
    }

    public AuthResponse deleteCartById(
            Long cartId,
            String userId,
            String sessionId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (userId != null) {
            if (cart.getUser() == null || !cart.getUser().getId().equals(userId)) {
                 return new AuthResponse(HttpStatus.NOT_FOUND.value(), "User not found", null);
            }
        } else if (sessionId != null) {
            if (cart.getSessionId() == null || !cart.getSessionId().equals(sessionId)) {
                return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Session user not found", null);
            }
        } else {
            throw new RuntimeException("Either userId or sessionId must be provided");
        }

        cartItemRepository.deleteAll(cart.getItems());
        cartRepo.delete(cart);

        return new AuthResponse(HttpStatus.OK.value(), "deleted", null);
    }


    public AuthResponse deleteAllCarts(String userId, String sessionId) {
        if (userId != null) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            List<Cart> userCarts = cartRepo.findByUser(user);
            userCarts.forEach(cart -> {
                cartItemRepository.deleteAll(cart.getItems());
                cartRepo.delete(cart);
            });

        } else if (sessionId != null) {
            List<Cart> guestCarts = cartRepo.findBySessionId(sessionId);
            guestCarts.forEach(cart -> {
                cartItemRepository.deleteAll(cart.getItems());
                cartRepo.delete(cart);
            });

        } else {
            return new AuthResponse(HttpStatus.OK.value(), "Either userId or sessionId must be provided", null);
        }

        return new AuthResponse(HttpStatus.OK.value(), "deleted", null);
    }


    public AuthResponse updateCartItem(String userId, String sessionId, Long productId, int quantity) {
        Cart cart;

        if (userId != null) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            cart = cartRepo.findByUserAndStatus(user, Cart.Status.ACTIVE)
                    .orElseThrow(() -> new EntityNotFoundException("User cart not found"));

        } else if (sessionId != null) {
            cart = cartRepo.findBySessionIdAndStatus(sessionId, Cart.Status.ACTIVE)
                    .orElseThrow(() -> new EntityNotFoundException("Guest cart not found"));

        } else {
            return new AuthResponse(HttpStatus.OK.value(), "Either userId or sessionId must be provided", null);
        }

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setCartQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return new AuthResponse(HttpStatus.OK.value(), "updated",null);
    }


}
