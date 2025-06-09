package com.sample.sample.Controller;




import com.sample.sample.Model.ProductCustomization;
import com.sample.sample.Service.ProductCustomizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product-customizations")
@CrossOrigin("*")
public class ProductCustomizationController {

    @Autowired
    private ProductCustomizationService service;

    // POST: Save product customization with images and JSON data
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveCustomization(
            @RequestParam("jsonData") String jsonData,
            @RequestParam("bannerImage") MultipartFile bannerImage,
            @RequestParam("thumbnails") List<MultipartFile> thumbnails) {
        try {
            ProductCustomization saved = service.saveCustomization(jsonData, bannerImage, thumbnails);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save customization: " + e.getMessage());
        }
    }

    // GET: Return all saved customizations
    @GetMapping
    public ResponseEntity<List<ProductCustomization>> getAllCustomizations() {
        List<ProductCustomization> customizations = service.getAllCustomizations();
        return ResponseEntity.ok(customizations);
    }




}


