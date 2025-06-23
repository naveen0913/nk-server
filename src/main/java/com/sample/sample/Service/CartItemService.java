package com.sample.sample.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.DTO.CartDTO;
import com.sample.sample.Model.CartItem;
import com.sample.sample.Model.Products;
import com.sample.sample.Model.User;
import com.sample.sample.Repository.CartItemRepository;
import com.sample.sample.Repository.ProductsRepository;
import com.sample.sample.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public CartItem addCartItem(Long productId, String userId, String cartPayload, List<MultipartFile> customImages) throws IOException {
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

        // Fetch Product (Images)
        Products product = productsRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found"));

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
        }else{
            cartItem.setCustomImages(imageUrls.isEmpty() ? null : imageUrls);
        }
        cartItem.setCustomImages(imageUrls);
        cartItem.setLabelDesigns(cartDTO.getLabelDesigns());

        // Save Cart Item
        return cartItemRepository.save(cartItem);
    }

    // ✅ Update Cart Item by cartItemId
    @Transactional
    public CartItem updateCartItemById(Long cartItemId, String cartPayload, List<MultipartFile> customImages) throws IOException {
        // Convert JSON string to DTO
        ObjectMapper objectMapper = new ObjectMapper();
        CartDTO cartDTO = objectMapper.readValue(cartPayload, CartDTO.class);

        // Fetch existing Cart Item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart Item not found with id: " + cartItemId));

        // Update fields
        cartItem.setCartItemName(cartDTO.getCartItemName());
        cartItem.setCartQuantity(cartDTO.getCartQuantity());
        cartItem.setCartGiftWrap(cartDTO.isCartGiftWrap());
        cartItem.setTotalPrice(cartDTO.getTotalPrice());
        cartItem.setCustomName(cartDTO.getCustomName());
        cartItem.setOptionCount(cartDTO.getOptionCount());
        cartItem.setOptionPrice(cartDTO.getOptionPrice());
        cartItem.setOptiondiscount(cartDTO.getOptiondiscount());
        cartItem.setOptiondiscountPrice(cartDTO.getOptiondiscountPrice());
        cartItem.setLabelDesigns(cartDTO.getLabelDesigns());

        // Process Images
        List<String> imageUrls = cartItem.getCustomImages() != null ? cartItem.getCustomImages() : new ArrayList<>();

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

        cartItem.setCustomImages(imageUrls);

        // Save updated Cart Item
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getUserCartList(String userId) {
        return cartItemRepository.getAllItemsByUser(userId);
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart Item not found with id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    public void deleteAllCartItems() {
        cartItemRepository.deleteAll();
    }

    public long getCartItemsCount() {
        return cartItemRepository.count();
    }
}
