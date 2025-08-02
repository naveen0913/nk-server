package com.sample.sample.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.DTO.CartDTO;
import com.sample.sample.Model.CartItem;
import com.sample.sample.Model.Products;
import com.sample.sample.Model.User;
import com.sample.sample.Repository.CartItemRepository;
import com.sample.sample.Repository.ProductsRepository;
import com.sample.sample.Repository.UserRepo;
import com.sample.sample.Responses.AuthResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductsRepository productsRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public AuthResponse addCartItem(Long productId, String userId, String cartPayload, List<MultipartFile> customImages) throws IOException {
        // Convert JSON string to DTO
        ObjectMapper objectMapper = new ObjectMapper();
        CartDTO cartDTO = objectMapper.readValue(cartPayload, CartDTO.class);

        // Map DTO to Entity
        CartItem cartItem = new CartItem();
        cartItem.setCartItemName(cartDTO.getCartItemName());
        cartItem.setCartQuantity(cartDTO.getCartQuantity());
        cartItem.setCartGiftWrap(cartDTO.isCartGiftWrap());
        cartItem.setTotalPrice(cartDTO.getTotalPrice());
        cartItem.setCustomName(cartDTO.getCustomName());
        cartItem.setOptionCount(cartDTO.getOptionCount());
        cartItem.setOptionPrice(cartDTO.getOptionPrice());
        cartItem.setOptiondiscount(cartDTO.getOptiondiscount());
        cartItem.setOptiondiscountPrice(cartDTO.getOptiondiscountPrice());

        // Fetch User
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch Product
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartItem.setUser(user);
        cartItem.setProduct(product);

        // Process Images
        List<String> imageUrls = new ArrayList<>();
        if (customImages != null && !customImages.isEmpty()) {
            for (MultipartFile file : customImages) {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir + fileName);
                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, file.getBytes());
                    imageUrls.add(fileName);
                }
            }
        }

        cartItem.setCustomImages(imageUrls.isEmpty() ? null : imageUrls);
        cartItem.setLabelDesigns(cartDTO.getLabelDesigns());

        // Save Cart Item
        cartItemRepository.save(cartItem);

        return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
    }


    @Transactional
    public AuthResponse updateCartItemById(Long cartItemId, String cartPayload, List<MultipartFile> customImages) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CartDTO cartDTO = objectMapper.readValue(cartPayload, CartDTO.class);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart Item not found with id: " + cartItemId));

        if (cartDTO.getCustomName() != null) {
            cartItem.setCustomName(cartDTO.getCustomName());
        }
        cartItem.setCartGiftWrap(cartDTO.isCartGiftWrap());
        cartItem.setCartQuantity(cartDTO.getCartQuantity());

        if (cartDTO.getLabelDesigns() != null) {
            cartItem.setLabelDesigns(cartDTO.getLabelDesigns());
        }

        if (customImages != null && !customImages.isEmpty()) {
            // Delete old images
            List<String> existingImages = cartItem.getCustomImages();
            if (existingImages != null) {
                for (String fileName : existingImages) {
                    Path filePath = Paths.get(uploadDir + fileName);
                    try {
                        Files.deleteIfExists(filePath);
                    } catch (IOException e) {
                        System.err.println("Failed to delete old image: " + filePath);
                    }
                }
            }

            // Save new images
            List<String> newImageUrls = new ArrayList<>();
            for (MultipartFile file : customImages) {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir + fileName);
                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, file.getBytes());
                    newImageUrls.add(fileName);
                }
            }
            cartItem.setCustomImages(newImageUrls);
        }

        cartItemRepository.save(cartItem);

        return new AuthResponse(HttpStatus.OK.value(), "updated", null);
    }


    public AuthResponse getUserCartList(String userId) {
        List<CartItem> userCartItems = cartItemRepository.getAllItemsByUser(userId);
        return new AuthResponse(HttpStatus.OK.value(), "fetched", userCartItems);
    }

    public AuthResponse getAllCartItems() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        return new AuthResponse(HttpStatus.OK.value(), "fetched", cartItems);
    }

    public AuthResponse getAllUsers() {
        List<User> users = userRepo.findAll();
        return new AuthResponse(HttpStatus.OK.value(), "fetched", users);
    }


    public AuthResponse deleteCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Cart Item not found with id: " + cartItemId, null);
        }
        cartItemRepository.deleteById(cartItemId);
        return new AuthResponse(HttpStatus.OK.value(), "deleted", null);
    }

    public AuthResponse deleteAllCartItems() {
        cartItemRepository.deleteAll();
        return new AuthResponse(HttpStatus.OK.value(), "all cart items deleted", null);
    }

    public AuthResponse getCartItemsCount() {
        long count = cartItemRepository.count();
        return new AuthResponse(HttpStatus.OK.value(), "ok", count);
    }

}
