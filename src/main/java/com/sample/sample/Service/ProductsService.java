package com.sample.sample.Service;

import com.sample.sample.Model.Products;
import com.sample.sample.Repository.ProductsRepository;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.ImageResponse;
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
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public AuthResponse saveProducts(String name, String description, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        Products data = new Products();
        data.setProductName(name);
        data.setProductDescription(description);
        data.setProductUrl("/uploads/" + filename);

        productsRepository.save(data);

        return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
    }

    public AuthResponse getAllProducts() {
        List<Products> productList = productsRepository.findAll();
        if (productList.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "products not found", null);
        }
        List<ImageResponse> responseList = new ArrayList<>();
        String baseUrl = "http://localhost:8081";
        for (Products product : productList) {
            String imageUrl = product.getProductUrl();
            String finalUrl = (imageUrl != null && !imageUrl.isEmpty()) ? baseUrl + imageUrl : null;

            responseList.add(new ImageResponse(
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductDescription(),
                    finalUrl,
                    product.getProductCustomization()
            ));
        }

        return new AuthResponse(HttpStatus.OK.value(), "created", responseList);
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


    @Transactional
    public AuthResponse updateProductById(Long id, String name, String description, MultipartFile file) throws IOException {
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

        productsRepository.save(existingProduct);

        return new AuthResponse(HttpStatus.OK.value(), "created", null);
    }




}
