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


    public Products saveProducts(String name, String description, MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        Products data = new Products();
        data.setProductName(name);
        data.setProductDescription(description);
        data.setProductUrl("/uploads/" + filename); // Accessible from browser

        return productsRepository.save(data);
    }

    public List<Products> getAllProducts() {
        return productsRepository.findAll();
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


    public Products updateProductById(Long id, String name, String description, MultipartFile file) throws IOException {
        // 1. Find existing product
        Products existingProduct = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // 2. Update name if provided
        if (name != null && !name.isBlank()) {
            existingProduct.setProductName(name);
        }

        // 3. Update description if provided
        if (description != null && !description.isBlank()) {
            existingProduct.setProductDescription(description);
        }

        // 4. Update image if file is present
        if (file != null && !file.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            existingProduct.setProductUrl("/uploads/" + filename);
        }

        // 5. Save and return updated product
        return productsRepository.save(existingProduct);
    }




}
