package com.sample.sample.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.DTO.CartDTO;
import com.sample.sample.Model.CartItem;
import com.sample.sample.Model.CustomizationOption;
import com.sample.sample.Model.Products;
import com.sample.sample.Model.User;
import com.sample.sample.Repository.CartItemRepository;
import com.sample.sample.Repository.CustomOptionRepository;
import com.sample.sample.Repository.ProductsRepository;
import com.sample.sample.Repository.UserRepo;
import com.sample.sample.Responses.AuthResponse;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private  MailService mailService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private CustomOptionRepository customOptionRepository;

    @Transactional
    public AuthResponse addCartItem(Long optionId,Long productId, String userId, String cartPayload, List<MultipartFile> customImages) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        CartDTO cartDTO = objectMapper.readValue(cartPayload, CartDTO.class);


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

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        CustomizationOption option = customOptionRepository.findById(optionId)
                                .orElseThrow(() -> new EntityNotFoundException("Custom Option not found"));

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setCustomizationOption(option);

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
        cartItem.setDesigns(cartDTO.getCartItemDesigns());

        cartItemRepository.save(cartItem);

        String toEmail = user.getEmail(); // assumes User has getEmail()
        String userName = user.getUsername(); // or getUserName()
        String productName = product.getProductName();
        int quantity = cartDTO.getCartQuantity();

//        mailService.sendCartItemAddedMail(toEmail, userName, productName, quantity);
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

//        if (cartDTO.getCartItemDesigns() != null) {
//            cartItem.setDesigns(cartDTO.getCartItemDesigns());
//        }

        if (customImages != null && !customImages.isEmpty()) {

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
        User user = cartItem.getUser();
        String toEmail = user.getEmail();
        String userName = user.getUsername();
        String productName = cartItem.getProduct().getProductName();
        int quantity = cartItem.getCartQuantity();

//        mailService.sendCartItemUpdatedMail(toEmail, userName, productName, quantity);
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
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);

        if (optionalCartItem.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Cart Item not found with id: " + cartItemId, null);
        }

        CartItem cartItem = optionalCartItem.get();


        User user = cartItem.getUser();
        String toEmail = user.getEmail();
        String username = user.getUsername(); // use getName() or appropriate method
        String productName = cartItem.getProduct().getProductName();

        cartItemRepository.deleteById(cartItemId);

//        mailService.sendCartItemDeletedMail(toEmail, username, productName);

        return new AuthResponse(HttpStatus.OK.value(), "Cart item deleted and email sent", null);
    }


    @Transactional
    public AuthResponse deleteAllCartItems(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> userCartItems = cartItemRepository.getAllItemsByUser(userId);

        if (userCartItems.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "No items found in cart", null);
        }

        String toEmail = user.getEmail();
        String username = user.getUsername();

        List<String> productNames = userCartItems.stream()
                .map(item -> item.getProduct().getProductName())
                .toList();


//        mailService.sendCartClearedMail(toEmail, username, productNames);
        cartItemRepository.deleteAll(userCartItems);

        return new AuthResponse(HttpStatus.OK.value(), "All cart items deleted and email sent", null);
    }

    public AuthResponse getCartItemsCount() {
        long count = cartItemRepository.count();
        return new AuthResponse(HttpStatus.OK.value(), "ok", count);
    }

}
