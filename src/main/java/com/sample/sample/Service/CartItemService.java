package com.sample.sample.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.DTO.CartDTO;
import com.sample.sample.DTO.ProductCustomizationDTO;
import com.sample.sample.Model.CartItem;
import com.sample.sample.Model.Images;
import com.sample.sample.Model.User;
import com.sample.sample.Repository.CartItemRepository;
import com.sample.sample.Repository.ImageRepo;
import com.sample.sample.Repository.UserRepo;
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
    private ImageRepo imageRepo;

    @Value("${upload.path:uploads}")
    private String uploadPath;

    private final ObjectMapper mapper = new ObjectMapper();

    public CartItem addCartItem(Long productId, String userId, String cartItemDTO, List<MultipartFile> customImages) throws IOException {
        CartItem cartItem = new CartItem();
        CartDTO dto = mapper.readValue(cartItemDTO, CartDTO.class);

        Images product = imageRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        // Fetch user by ID
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartItem.setUser(user);
        cartItem.setProduct(product);

        cartItem.setCartItemName(dto.getCartItemName());
        cartItem.setCartQuantity(dto.getCartQuantity());
        cartItem.setCartGiftWrap(dto.isCartGiftWrap());
        cartItem.setLabelDesigns(dto.getLabelDesigns());
        cartItem.setOptionCount(dto.getOptionCount());
        cartItem.setOptionPrice(dto.getOptionPrice());
        cartItem.setOptiondiscount(dto.getOptiondiscount());
        cartItem.setTotalPrice(dto.getTotalPrice());
cartItem.setCustomName(dto.getCartItemName());

        // Handle image upload
        List<String> imageUrls = new ArrayList<>();
        if (customImages != null && !customImages.isEmpty()) {
            for (MultipartFile file : customImages) {
                try {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadPath, filename);
                    Files.write(filePath, file.getBytes());
                    imageUrls.add("/uploads/" + filename); // adjust if your static mapping is different
                } catch (IOException e) {
                    throw new RuntimeException("Error saving image: " + e.getMessage());
                }
            }
        }
        cartItem.setCustomImages(imageUrls);


        return cartItemRepository.save(cartItem);
    }









}
