package com.sample.sample.Service;

import com.sample.sample.Model.Images;
import com.sample.sample.Repository.ImageRepo;
import com.sample.sample.Responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ImgService {

    @Autowired
    private  ImageRepo imageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public Images saveImage(String name, String description, MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/" + uploadDir + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        Images data = new Images();
        data.setName(name);
        data.setDescription(description);
        data.setImageUrl("/" + uploadDir + filename);

        return imageRepository.save(data);
    }

    public List<Images> getAllImages() {
        return imageRepository.findAll();
    }

    public AuthResponse getProductById(Long productId){
        Optional<Images> existedProduct = imageRepository.findById(productId);
        if (!existedProduct.isPresent()){
            return new AuthResponse(HttpStatus.NOT_FOUND.value(),"Product Not found",null);
        }
        return new AuthResponse(HttpStatus.OK.value(),"success",existedProduct);
    }

    public AuthResponse updateProduct(Long productId,String name, String description, MultipartFile file){
        Optional<Images> existedProduct = imageRepository.findById(productId);
        if (!existedProduct.isPresent()){
            return new AuthResponse(HttpStatus.NOT_FOUND.value(),"Product Not found",null);
        }

        Images existingImage = existedProduct.get();
        existingImage.setName(name);
        existingImage.setDescription(description);
        if (file != null && !file.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static/" + uploadDir + filename);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                existingImage.setImageUrl("/" + uploadDir + filename);
            } catch (IOException e) {
                return new AuthResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),null);
            }
        }
        imageRepository.save(existingImage);
        return new AuthResponse(HttpStatus.OK.value(),"updated",existingImage);

    }


    public AuthResponse deleteImage(Long id) {

        Optional<Images> existedProduct = imageRepository.findById(id);
        if (!existedProduct.isPresent()){
            return new AuthResponse(HttpStatus.NOT_FOUND.value(),"Product Not found",null);
        }

        Images image = existedProduct.get();
        // Optionally delete file from storage (if required)
        try {
            Path path = Paths.get("src/main/resources/static/uploads" + image.getImageUrl());
            Files.deleteIfExists(path);
        } catch (IOException e) {
            return new AuthResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),null);
        }
        imageRepository.deleteById(id);
        return new AuthResponse(HttpStatus.OK.value(),"deleted",null);
    }



}

