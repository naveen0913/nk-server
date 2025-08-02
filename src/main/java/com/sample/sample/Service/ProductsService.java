package com.sample.sample.Service;

import com.sample.sample.Model.Products;
import com.sample.sample.Repository.ProductsRepository;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public AuthResponse saveProducts(String name, String description, MultipartFile file) {
        try {

            if (file == null || file.isEmpty()) {
                return new AuthResponse(400, "Product image file is required", null);
            }


            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            // 3. Create and populate product
            Products data = new Products();
            data.setProductName(name);
            data.setProductDescription(description);
            data.setProductUrl("/uploads/" + filename); // Accessible from browser

            // 4. Save to repository
            Products savedProduct = productsRepository.save(data);

            // 5. Return success response
            return new AuthResponse(201, "Product created successfully", savedProduct);

        } catch (IOException e) {
            return new AuthResponse(500, "Failed to save product image", null);
        } catch (Exception e) {
            return new AuthResponse(500, "An unexpected error occurred", null);
        }
    }


    public AuthResponse getAllProducts() {
        try {
            List<Products> productList = productsRepository.findAll();

            if (productList.isEmpty()) {
                return new AuthResponse(204, "No products found", productList);
            }

            return new AuthResponse(200, "Products retrieved successfully", productList);

        } catch (Exception e) {
            return new AuthResponse(500, "An unexpected error occurred", null);
        }
    }


    public AuthResponse getProductById(Long id){
        Optional<Products> existedProduct = productsRepository.findById(id);
        if (!existedProduct.isPresent()){
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "product not found",null);
        }
        ImageResponse imageResponse = new ImageResponse(existedProduct.get().getProductId(), existedProduct.get().getProductName(),existedProduct.get().getProductDescription(),existedProduct.get().getProductUrl(),existedProduct.get().getProductCustomization());
        return new AuthResponse(HttpStatus.OK.value(), "success",imageResponse);

    }

    public AuthResponse deleteProduct(Long id){
        Optional<Products> existedProduct = productsRepository.findById(id);
        if (!existedProduct.isPresent()){
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "product not found",null);
        }
        productsRepository.deleteById(id);
        return new AuthResponse(HttpStatus.OK.value(), "deleted",null);
    }


    public AuthResponse updateProductById(Long id, String name, String description, MultipartFile file) {
        try {

            Products existingProduct = productsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));


            if (name != null && !name.isBlank()) {
                existingProduct.setProductName(name);
            }
            if (description != null && !description.isBlank()) {
                existingProduct.setProductDescription(description);
            }


            if (file != null && !file.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(filename);
                Files.write(filePath, file.getBytes());

                existingProduct.setProductUrl("/uploads/" + filename);
            }


            Products updatedProduct = productsRepository.save(existingProduct);


            return new AuthResponse(200, "Product updated successfully", updatedProduct);

        } catch (IOException e) {
            return new AuthResponse(500, "Failed to update product image", null);
        } catch (RuntimeException e) {
            return new AuthResponse(404, e.getMessage(), null);
        } catch (Exception e) {
            return new AuthResponse(500, "An unexpected error occurred", null);
        }
    }




}
