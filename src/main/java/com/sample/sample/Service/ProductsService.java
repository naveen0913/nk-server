package com.sample.sample.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.*;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.CustomImageResponse;
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
import java.util.Map;
import java.util.Optional;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserOrderedItemRepository userOrderedItemRepository;

    @Autowired
    private CustomizationImageRepo customizationImageRepo;

    @Autowired
    private HotspotRepo hotspotRepo;

    public AuthResponse saveProducts(String name, String description, String shapeType, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return new AuthResponse(HttpStatus.BAD_REQUEST.value(), "File is required", null);
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        Products product = new Products();
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductUrl("/uploads/" + filename);
        product.setProductOrdered(false);

        product.setProductShapeType(shapeType);

        int randomNum = (int) (Math.random() * 9000) + 1000;
        String customProductId = "PRODUCT" + randomNum;
        product.setCustomProductId(customProductId);

        product.setProductStatus(productStatus.active);

        productsRepository.save(product);

        return new AuthResponse(HttpStatus.CREATED.value(), "Product created successfully", product);
    }


    public AuthResponse getAllProducts() throws JsonProcessingException {
        List<Products> productList = productsRepository.findAll();
        if (productList.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "products not found", null);
        }
        List<ImageResponse> responseList = new ArrayList<>();
        String baseUrl = "http://localhost:8081";
        for (Products product : productList) {
            String imageUrl = product.getProductUrl();
            String finalUrl = (imageUrl != null && !imageUrl.isEmpty()) ? baseUrl + imageUrl : null;
            List<Design> designs = product.getDesigns();
            List<CustomImageResponse> customImageResponses = buildCustomImageResponses(product.getProductId());

            responseList.add(new ImageResponse(
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductDescription(),
                    finalUrl,
                    product.isProductOrdered(),
                    product.getProductStatus(),
                    product.getCustomProductId(),
                    product.getCreatedTime(),
                    product.getUpdatedTime(),
                    product.getProductCustomization(),
                    designs,
                    customImageResponses
            ));
        }

        return new AuthResponse(HttpStatus.OK.value(), "ok", responseList);
    }


    public AuthResponse getProductById(Long id) throws JsonProcessingException {
        Optional<Products> existedProduct = productsRepository.findById(id);
        if (!existedProduct.isPresent()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "product not found", null);
        }
        List<Design> designList = existedProduct.get().getDesigns();
        List<CustomImageResponse> customImageResponses = buildCustomImageResponses(id);

        ImageResponse imageResponse = new ImageResponse(
                existedProduct.get().getProductId(),
                existedProduct.get().getProductName(),
                existedProduct.get().getProductDescription(),
                existedProduct.get().getProductUrl(),
                existedProduct.get().isProductOrdered(),
                existedProduct.get().getProductStatus(),
                existedProduct.get().getCustomProductId(),
                existedProduct.get().getCreatedTime(),
                existedProduct.get().getUpdatedTime(),
                existedProduct.get().getProductCustomization(),
                designList,
                customImageResponses
        );
        return new AuthResponse(HttpStatus.OK.value(), "success", imageResponse);

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


    @Transactional
    public AuthResponse updateProductById(Long id, String name, String description, String shape, MultipartFile file) throws IOException {
        Products existingProduct = productsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (name != null && !name.isBlank()) {
            existingProduct.setProductName(name);
        }

        if (description != null && !description.isBlank()) {
            existingProduct.setProductDescription(description);
        }

        if (shape != null && !shape.isBlank()) {
            existingProduct.setProductShapeType(shape);
        }

        if (file != null && !file.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            existingProduct.setProductUrl("/uploads/" + filename);
        }

        productsRepository.save(existingProduct);

        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
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

//        if (product.getProductStatus() == productStatus.active) {
//            product.setProductStatus(productStatus.inactive);
//        } else if (product.getProductStatus() == productStatus.inactive) {
//            product.setProductStatus(productStatus.active);
//        } else {
//            return new AuthResponse(HttpStatus.BAD_REQUEST.value(), "Unknown product status", null);
//        }

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

    private List<CustomImageResponse> buildCustomImageResponses(Long productId) throws JsonProcessingException {
        List<ProductCustomizationImage> customizationImages = customizationImageRepo.findAllByProduct_Id(productId);
        ObjectMapper mapper = new ObjectMapper();
        List<CustomImageResponse> imageResponses = new ArrayList<>();

        for (ProductCustomizationImage img : customizationImages) {
            CustomImageResponse imageResponse = new CustomImageResponse();
            imageResponse.setId(img.getId());
            imageResponse.setImageUrl(img.getCustomImage());

            List<Hotspot> hotspots = hotspotRepo.findByProductCustomImageId(img.getId());
            List<List<CustomImageResponse.CoordinateResponse>> hotspotShapes = new ArrayList<>();

            for (Hotspot h : hotspots) {
                List<Map<String, Object>> pointList =
                        mapper.readValue(h.getCoordinates(), new TypeReference<List<Map<String, Object>>>() {
                        });
                List<CustomImageResponse.CoordinateResponse> coords = new ArrayList<>();

                for (Map<String, Object> point : pointList) {
                    CustomImageResponse.CoordinateResponse coord = new CustomImageResponse.CoordinateResponse();
//                    coord.setId(coord.getId());
                    coord.setX(((Number) point.get("x")).doubleValue());
                    coord.setY(((Number) point.get("y")).doubleValue());
                    coord.setShape(h.getShapeType());
                    coords.add(coord);
                }

                hotspotShapes.add(coords);
            }

            imageResponse.setHotspots(hotspotShapes);
            imageResponses.add(imageResponse);
        }

        return imageResponses;
    }


}
