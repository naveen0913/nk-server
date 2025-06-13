package com.sample.sample.Controller;

import com.sample.sample.Model.Images;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Responses.ImageResponse;
import com.sample.sample.Service.ImgService;
import jakarta.persistence.Access;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/image")
@CrossOrigin("*")
public class ImageController {

    @Autowired
    private  ImgService imageService;


    @PostMapping
    public ResponseEntity<Images> uploadImage(@RequestParam("name") String name,
                                              @RequestParam("description") String description,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.saveImage(name, description, file));
    }

//
//    @GetMapping
//    public List<ImageResponse> listImages() throws IOException {
//        List<Images> imagesList = imageService.getAllImages();
//        List<ImageResponse> responseList = new ArrayList<>();
//
//        String folderPath = new ClassPathResource("static/uploads").getFile().getAbsolutePath();
//        File folder = new File(folderPath);
//        File[] files = folder.listFiles();
//
//
//        for (Images image : imagesList) {
//            String baseUrl = "http://localhost:8081";
//            String uploadPath = "uploads";
//            String finalUrl = buildImageUrl(baseUrl,uploadPath,image.getImageUrl());
////            String url = "http://localhost:8080/uploads/" + image.getImageUrl(); // Assuming `getFilename()` exists
//            responseList.add(new ImageResponse(image.getId(),image.getName(), image.getDescription(), finalUrl,));
//        }
//
//        return responseList;
//
//    }

    @GetMapping("/{productId}")
    public AuthResponse getProductById(@PathVariable Long productId){
        return imageService.getProductById(productId);
    }


    @PutMapping("/{productId}")
    public AuthResponse updateImage(
            @PathVariable Long productId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile file
    )  {
        return imageService.updateProduct(productId, name, description, file);
    }

    @DeleteMapping("/{productId}")
    public AuthResponse  deleteImage(@PathVariable Long productId) {
        return imageService.deleteImage(productId);
    }


    public String buildImageUrl(String baseUrl, String uploadPath, String filename) {
        // Ensure no double slashes in the final URL
        if (filename.startsWith("/") || filename.startsWith(uploadPath)) {
            filename = filename.replaceFirst("^/+", "").replaceFirst("^" + uploadPath + "/?", "");
        }
        return baseUrl + "/" + uploadPath + "/" + filename;
    }


}
