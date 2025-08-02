package com.sample.sample.Controller;

import com.sample.sample.Model.Products;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.ImageResponse;
import com.sample.sample.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse addProduct(@RequestParam("name") String name,
                                    @RequestParam("description") String description,
                                    @RequestParam("file") MultipartFile file) throws IOException {
        productsService.saveProducts(name, description, file);
        return new AuthResponse(HttpStatus.CREATED.value(), "created",null);

    }



    @GetMapping
    public ResponseEntity<AuthResponse> getAllProducts() {
        AuthResponse serviceResponse = productsService.getAllProducts();

        if (serviceResponse.getData() == null) {
            return ResponseEntity.status(serviceResponse.getCode()).body(serviceResponse);
        }

        List<Products> productsList = (List<Products>) serviceResponse.getData();
        List<ImageResponse> responseList = new ArrayList<>();

        String baseUrl = "http://localhost:8081";

        for (Products product : productsList) {
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

        AuthResponse finalResponse = new AuthResponse(
                serviceResponse.getCode(),
                serviceResponse.getMessage(),
                responseList
        );

        return ResponseEntity.status(finalResponse.getCode()).body(finalResponse);
    }


    @GetMapping("/{productId}")
    public AuthResponse getProductById(@PathVariable Long productId){
        return productsService.getProductById(productId);
    }

    @DeleteMapping("/{productId}")
    public AuthResponse deleteProductById(@PathVariable Long productId){
        return productsService.deleteProduct(productId);
    }

    @PutMapping("/{productId}")
    public AuthResponse updateProduct(
            @PathVariable Long productId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        productsService.updateProductById(productId, name, description, file);
        return new AuthResponse(HttpStatus.OK.value(), "updated", null);
    }

    public String buildImageUrl(String baseUrl, String uploadPath, String filename) {

        if (filename.startsWith("/") || filename.startsWith(uploadPath)) {
            filename = filename.replaceFirst("^/+", "").replaceFirst("^" + uploadPath + "/?", "");
        }
        return baseUrl + "/" + uploadPath + "/" + filename;
    }
}
