package com.sample.sample.Controller;

import com.sample.sample.Model.Images;
import com.sample.sample.Model.Products;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.ImageResponse;
import com.sample.sample.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
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
    public List<ImageResponse> getAllProducts() throws IOException {
        List<Products> productsList = productsService.getAllProducts();
        List<ImageResponse> responseList = new ArrayList<>();

        String folderPath = new ClassPathResource("static/uploads").getFile().getAbsolutePath();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();


        for (Products image : productsList) {
            String baseUrl = "http://localhost:8081";
            String uploadPath = "uploads";
            String finalUrl = buildImageUrl(baseUrl,uploadPath,image.getProductUrl());
//            String url = "http://localhost:8080/uploads/" + image.getImageUrl(); // Assuming `getFilename()` exists
            responseList.add(new ImageResponse(image.getProductId(),image.getProductName(), image.getProductDescription(), finalUrl,image.getProductCustomization()));
        }

        return responseList;

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
    public AuthResponse updateProduct(@PathVariable Long productId,
            @RequestParam("name") String name,
                                      @RequestParam("description") String description,
                                      @RequestParam("file") MultipartFile file) throws IOException {
        productsService.updateProductById(productId,name,description,file);
        return new AuthResponse(HttpStatus.OK.value(), "updated",null);

    }

    public String buildImageUrl(String baseUrl, String uploadPath, String filename) {
        // Ensure no double slashes in the final URL
        if (filename.startsWith("/") || filename.startsWith(uploadPath)) {
            filename = filename.replaceFirst("^/+", "").replaceFirst("^" + uploadPath + "/?", "");
        }
        return baseUrl + "/" + uploadPath + "/" + filename;
    }
}
