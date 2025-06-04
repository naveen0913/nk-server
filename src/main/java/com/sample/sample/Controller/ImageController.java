package com.sample.sample.Controller;

import com.sample.sample.Model.Images;
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

//    @GetMapping
//    public List<String> listImages() throws IOException {
//        String folderPath = new ClassPathResource("static/uploads").getFile().getAbsolutePath();
//        File folder = new File(folderPath);
//        File[] files = folder.listFiles();
//
//        List<String> imageUrls = new ArrayList<>();
//        if (files != null) {
//            for (File file : files) {
//                String filename = file.getName();
//                String url = "http://localhost:8080/uploads/" + filename;
//                imageUrls.add(url);
//            }
//        }
//        return imageUrls;
//    }

    @GetMapping
    public List<ImageResponse> listImages() throws IOException {
        List<Images> imagesList = imageService.getAllImages();
        List<ImageResponse> responseList = new ArrayList<>();

        String folderPath = new ClassPathResource("static/uploads").getFile().getAbsolutePath();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();


        for (Images image : imagesList) {
            String baseUrl = "http://localhost:8080";
            String uploadPath = "uploads";
            String finalUrl = buildImageUrl(baseUrl,uploadPath,image.getImageUrl());
//            String url = "http://localhost:8080/uploads/" + image.getImageUrl(); // Assuming `getFilename()` exists
            responseList.add(new ImageResponse(image.getId(),image.getName(), image.getDescription(), finalUrl));
        }


        return responseList;


    }

    public String buildImageUrl(String baseUrl, String uploadPath, String filename) {
        // Ensure no double slashes in the final URL
        if (filename.startsWith("/") || filename.startsWith(uploadPath)) {
            filename = filename.replaceFirst("^/+", "").replaceFirst("^" + uploadPath + "/?", "");
        }

        return baseUrl + "/" + uploadPath + "/" + filename;
    }




}
