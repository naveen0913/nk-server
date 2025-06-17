package com.sample.sample.Controller;

import com.sample.sample.Model.ProductCustomization;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.ProductCustomizationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-customizations")
@CrossOrigin("*")
public class ProductCustomizationController {

    @Autowired
    private ProductCustomizationService service;

    // POST: Save product customization with images and JSON data
    @PostMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveCustomization(
            @PathVariable Long productId,
            @RequestParam("jsonData") String jsonData,
            @RequestParam("bannerImage") MultipartFile bannerImage,
            @RequestParam("thumbnails") List<MultipartFile> thumbnails) {
        try {
            ProductCustomization saved = service.saveCustomization(productId,jsonData, bannerImage, thumbnails);
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

    // GET: Return customization by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomizationById(@PathVariable Long id) {
        Optional<ProductCustomization> customization = service.getCustomizationById(id);
        if (customization.isPresent()) {
            return ResponseEntity.ok(customization.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customization not found with ID: " + id);
        }
    }

    @PutMapping("/{customizationId}")
    public ResponseEntity<?> updateCustomization(
            @PathVariable Long customizationId,
            @RequestParam("jsonData") String dtoJson,
            @RequestParam(value = "bannerImage", required = false) MultipartFile bannerImage,
            @RequestParam(value = "thumbnails", required = false) List<MultipartFile> thumbnails) {

        try {
            ProductCustomization updated = service.updateCustomization(customizationId, dtoJson, bannerImage, thumbnails);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating customization: " + e.getMessage());
        }
    }

    @DeleteMapping("/{customizationId}")
    public ResponseEntity<AuthResponse> deleteCustomization(@PathVariable Long customizationId) {
        try {
            service.deleteCustomization(customizationId);
            return ResponseEntity.ok(new AuthResponse(HttpStatus.OK.value(), "Deleted successfully", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }




}
