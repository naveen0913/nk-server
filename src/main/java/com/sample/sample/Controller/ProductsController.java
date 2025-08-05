package com.sample.sample.Controller;

import com.sample.sample.DTO.ProductIdRequest;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestParam("name") String name,
                                        @RequestParam("description") String description,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        AuthResponse response = productsService.saveProducts(name, description, file);
        return ResponseEntity.status(response.getCode()).body(response);

    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        AuthResponse serviceResponse = productsService.getAllProducts();
        return ResponseEntity.status(serviceResponse.getCode()).body(serviceResponse);
    }

    @GetMapping("/{productId}")
    public AuthResponse getProductById(@PathVariable Long productId) {
        return productsService.getProductById(productId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long productId) {
        AuthResponse authResponse = productsService.deleteProduct(productId);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        AuthResponse authResponse = productsService.updateProductById(productId, name, description, file);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

    public String buildImageUrl(String baseUrl, String uploadPath, String filename) {

        if (filename.startsWith("/") || filename.startsWith(uploadPath)) {
            filename = filename.replaceFirst("^/+", "").replaceFirst("^" + uploadPath + "/?", "");
        }
        return baseUrl + "/" + uploadPath + "/" + filename;
    }


    @PutMapping("/update-status/{productId}")
    public ResponseEntity<AuthResponse> updateProductStatus(
            @PathVariable Long productId,
            @RequestBody ProductIdRequest request) {

        AuthResponse response = productsService.updateProductStatus(productId, request.getStatus());
        return ResponseEntity.status(response.getCode()).body(response);
    }


}