package com.sample.sample.Controller;

import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/designs")
@CrossOrigin
public class DesignController {

    @Autowired
    private DesignService designService;

    @PostMapping
    public ResponseEntity<AuthResponse> createDesign(
            @RequestParam("productId") Long productId,
            @RequestParam("designName") String designName,
            @RequestParam("designUrl") String designUrl,
            @RequestParam("imageFiles") MultipartFile[] imageFiles
    ) throws IOException {
        AuthResponse response = designService.createDesign(productId, designName, designUrl, imageFiles);
        return ResponseEntity.status(response.getCode()).body(response);
    }


    @PutMapping("/{designId}")
    public ResponseEntity<AuthResponse> updateDesign(
            @PathVariable Long designId,
            @RequestParam(required = false) String designName,
            @RequestParam(required = false) String designUrl,
            @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles
    ) throws IOException {
        AuthResponse response = designService.updateDesign(designId, designName, designUrl, imageFiles);
        return ResponseEntity.status(response.getCode()).body(response);
    }


    @GetMapping
    public ResponseEntity<AuthResponse> getAllDesigns() {
        return ResponseEntity.ok(designService.getAllDesigns());
    }

    @GetMapping("/{designId}")
    public ResponseEntity<AuthResponse> getDesignById(@PathVariable Long designId) {
        return ResponseEntity.ok(designService.getDesignById(designId));
    }

    @DeleteMapping
    public ResponseEntity<AuthResponse> deleteAllDesigns() {
        return ResponseEntity.ok(designService.deleteAllDesigns());
    }

    @DeleteMapping("/{designId}")
    public ResponseEntity<AuthResponse> deleteDesignById(@PathVariable Long designId) {
        return ResponseEntity.ok(designService.deleteDesignById(designId));
    }
}



