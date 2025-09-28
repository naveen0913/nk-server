package com.sample.sample.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.sample.DTO.ProductIdRequest;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") double price,
            @RequestParam("discountPrice") double discountPrice,
            @RequestParam("subCategory") String subCategory,
            @RequestParam("pTag") String pTag,
            @RequestParam("inStock") boolean inStock,
            @RequestParam("totalQuantity") Long totalQuantity,
            @RequestParam("availableQuantity") Long availableQuantity,
            @RequestParam(value = "weight",required = false) String weight,
            @RequestParam(value = "weightUnit",required = false) String weightUnit,
            @RequestParam(value = "attributeName",required = false) String attributeName,
            @RequestParam(value = "attributeValue",required = false) String attributeValue,
            @RequestParam("files") MultipartFile[] files
    ) throws IOException {

        AuthResponse response = productsService.addProduct(
                name,
                description,
                category,
                price,
                discountPrice,
                subCategory,
                pTag,
                inStock,
                totalQuantity,
                availableQuantity,
                weight,
                weightUnit,attributeName,attributeValue,
                files
        );
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() throws JsonProcessingException {
        AuthResponse serviceResponse = productsService.getAllProducts();
        return ResponseEntity.status(serviceResponse.getCode()).body(serviceResponse);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) throws JsonProcessingException {
        AuthResponse authResponse = productsService.getProductById(productId);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long productId) {
        AuthResponse authResponse = productsService.deleteProduct(productId);
        return ResponseEntity.status(authResponse.getCode()).body(authResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double discountPrice,
            @RequestParam(required = false) String subCategory,
            @RequestParam(required = false) String pTag,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Long totalQuantity,
            @RequestParam(required = false) Long availableQuantity,
            @RequestParam(required = false) String weight,
            @RequestParam(required = false) String weightUnit,
            @RequestParam(required = false) String attributeName,
            @RequestParam(required = false) String attributeValue,
            @RequestParam(value = "files", required = false) MultipartFile[] files
    ) throws IOException {

        AuthResponse response = productsService.updateProduct(
                id, name, description, category, price, discountPrice,
                subCategory, pTag, inStock, totalQuantity, availableQuantity,
                weight, weightUnit, attributeName, attributeValue, files
        );

        return ResponseEntity.status(response.getCode()).body(response);
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