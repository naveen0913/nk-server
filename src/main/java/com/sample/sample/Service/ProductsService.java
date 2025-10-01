package com.sample.sample.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.*;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.ImageResponse;
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
import java.util.stream.Collectors;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserOrderedItemRepository userOrderedItemRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public AuthResponse addProduct(String name, String description,String category,double price,double discountPrice,String subCategory,String pTag,boolean inStock,Long totalQuantity,Long availableQuantity,String weight,String weightUnit,String attributeName,String attributeValue, MultipartFile[] files) throws IOException {

        Products product = new Products();
        product.setProductName(name);
        product.setpCategory(category);
        product.setProductDescription(description);
        product.setProductOrdered(false);
        product.setPrice(price);
        product.setDiscountPrice(discountPrice);
        product.setpTag(pTag);
        product.setAvailableQuantity(availableQuantity);
        product.setInStock(inStock);
        product.setTotalQuantity(totalQuantity);
        product.setpSubCategory(subCategory);
        product.setWeight(weight);
        product.setWeightUnit(weightUnit);
        product.setAttributeName(attributeName);
        product.setAttributeValue(attributeValue);
        product.setWishlisted(false);

        int randomNum = (int) (Math.random() * 9000) + 1000;
        String customProductId = "PRODUCT" + randomNum;
        product.setCustomProductId(customProductId);
        product.setProductStatus(productStatus.active);
        List<ProductImages> imagesList = new ArrayList<>();
//        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
//        Files.createDirectories(uploadPath);

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
//                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//                Path filePath = uploadPath.resolve(filename);
//                Files.write(filePath, file.getBytes());
                String cloudinaryFile = cloudinaryService.uploadFile(file);

                ProductImages images = new ProductImages();
//                images.setImageUrl("/uploads/" + filename);
                images.setImageUrl(cloudinaryFile);
                images.setProducts(product);
                imagesList.add(images);
                product.setProductImages(imagesList);
            }
        }
        productsRepository.save(product);

        return new AuthResponse(HttpStatus.CREATED.value(), "Product created successfully", product);
    }


    public AuthResponse getAllProducts() throws JsonProcessingException {
        List<Products> productList = productsRepository.findAll();
        if (productList.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "products not found", null);
        }
        List<ImageResponse> responseList = new ArrayList<>();
        for (Products product : productList) {
            List<ImageResponse.ProductImagesResponse> imageResponses = product.getProductImages()
                    .stream()
                    .map(image -> new ImageResponse.ProductImagesResponse(
                            image.getImageId(),
                            image.getImageUrl(),
                            product.getProductId()
                    ))
                    .collect(Collectors.toList());

            responseList.add(new ImageResponse(
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
            ));
        }

        return new AuthResponse(HttpStatus.OK.value(), "ok", responseList);
    }


    public AuthResponse getProductById(Long id) throws JsonProcessingException {
        Optional<Products> existedProduct = productsRepository.findById(id);
        if (!existedProduct.isPresent()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "product not found", null);
        }
        List<ImageResponse.ProductImagesResponse> imageResponses = existedProduct.get().getProductImages()
                .stream()
                .map(image -> new ImageResponse.ProductImagesResponse(
                        image.getImageId(),
                        image.getImageUrl(),
                        existedProduct.get().getProductId()
                ))
                .collect(Collectors.toList());

        ImageResponse imageResponse = new ImageResponse(
                existedProduct.get().getProductId(),
                existedProduct.get().getProductName(),
                existedProduct.get().getProductDescription(),
                existedProduct.get().isProductOrdered(),
                existedProduct.get().getProductStatus(),
                existedProduct.get().getCustomProductId(),
                existedProduct.get().getCreatedTime(),
                existedProduct.get().getUpdatedTime(),
                existedProduct.get().getpCategory(),
                existedProduct.get().getpSubCategory(),
                existedProduct.get().isInStock(),
                existedProduct.get().getTotalQuantity(),
                existedProduct.get().getAvailableQuantity(),
                existedProduct.get().getpTag(),
                existedProduct.get().getPrice(),
                existedProduct.get().getDiscountPrice(),
                existedProduct.get().getWeight(),
                existedProduct.get().getWeightUnit(),
                existedProduct.get().getAttributeName(),
                existedProduct.get().getAttributeValue(),
                imageResponses,
                existedProduct.get().isWishlisted()
                );
        return new AuthResponse(HttpStatus.OK.value(), "success", existedProduct);

    }

    public AuthResponse deleteProduct(Long id) {
        Products existedProduct = productsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

//        boolean hasOrders = userOrderedItemRepository.existsByProduct_ProductId(id);
        if (existedProduct.isProductOrdered() == true) {
            return new AuthResponse(HttpStatus.CONFLICT.value(), "Product is placed for Order.Cannot delete!", null);
        }

        productsRepository.deleteById(id);
        return new AuthResponse(HttpStatus.OK.value(), "Product deleted", null);
    }

    public AuthResponse updateProduct(
            Long id,
            String name,
            String description,
            String category,
            Double price,
            Double discountPrice,
            String subCategory,
            String pTag,
            Boolean inStock,
            Long totalQuantity,
            Long availableQuantity,
            String weight,
            String weightUnit,
            String attributeName,
            String attributeValue,
            MultipartFile[] files
    ) throws IOException {

        // Find existing product
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Update fields only if provided
        if (name != null) product.setProductName(name);
        if (description != null) product.setProductDescription(description);
        if (category != null) product.setpCategory(category);
        if (subCategory != null) product.setpSubCategory(subCategory);

        if (price != null) product.setPrice(price);
        if (discountPrice != null) product.setDiscountPrice(discountPrice);

        if (pTag != null) product.setpTag(pTag);
        if (inStock != null) product.setInStock(inStock);
        if (totalQuantity != null) product.setTotalQuantity(totalQuantity);
        if (availableQuantity != null) product.setAvailableQuantity(availableQuantity);

        if (weight != null) product.setWeight(weight);
        if (weightUnit != null) product.setWeightUnit(weightUnit);

        if (attributeName != null) product.setAttributeName(attributeName);
        if (attributeValue != null) product.setAttributeValue(attributeValue);

        // Handle file uploads if new files are provided
        if (files != null && files.length > 0) {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);

            List<ProductImages> newImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(filename);
                    Files.write(filePath, file.getBytes());

                    ProductImages img = new ProductImages();
                    img.setImageUrl("/uploads/" + filename);
                    img.setProducts(product);
                    newImages.add(img);
                }
            }
            // Decide whether to replace or append images
            product.getProductImages().addAll(newImages);
        }

        // Save updated product
        productsRepository.save(product);

        return new AuthResponse(HttpStatus.OK.value(), "Product updated successfully", product);
    }


    public AuthResponse activateProductById(Long productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getProductStatus() == productStatus.active) {
            return new AuthResponse(HttpStatus.OK.value(), "Product is already ACTIVE", null);
        }

        product.setProductStatus(productStatus.active);
        productsRepository.save(product); // updatedTime will auto-update

        return new AuthResponse(HttpStatus.OK.value(), "Product activated successfully", null);
    }


    public AuthResponse updateProductStatus(Long productId, String status) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));


        if (product.getProductStatus() == productStatus.active) {
            product.setProductStatus(productStatus.valueOf(status));
        } else if (product.getProductStatus() == productStatus.inactive) {
            product.setProductStatus(productStatus.valueOf(status));
        } else {
            return new AuthResponse(HttpStatus.BAD_REQUEST.value(), "Unknown product status", null);
        }

        productsRepository.save(product);
        return new AuthResponse(HttpStatus.OK.value(), "Product status toggled successfully", null);
    }



}
