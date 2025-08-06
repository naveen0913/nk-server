package com.sample.sample.Service;

import com.sample.sample.Model.Design;
import com.sample.sample.Model.DesignImage;
import com.sample.sample.Model.Products;
import com.sample.sample.Repository.DesignImageRepository;
import com.sample.sample.Repository.DesignRepo;
import com.sample.sample.Repository.ProductsRepository;
import com.sample.sample.Responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class DesignService {

    @Autowired
    private DesignRepo designRepository;

    @Autowired
    private DesignImageRepository designImageRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Value("${file.upload-dir}")
    private String uploadImagePath;

    public AuthResponse createDesign(Long productId, String designName, String designUrl, MultipartFile[] imageFiles) throws IOException {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Path uploadPath = Paths.get(uploadImagePath).toAbsolutePath();
        Files.createDirectories(uploadPath);

        Design design = new Design();
        design.setDesignName(designName);
        design.setDesignUrl(designUrl);
        design.setProduct(product);

        List<DesignImage> imageList = new ArrayList<>();

        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.write(filePath, file.getBytes());

                DesignImage image = new DesignImage();
                image.setDesignUrl("/uploads/" + filename); // assuming static resource path
                image.setDesign(design);
                imageList.add(image);
            }
        }

        design.setDesignImages(imageList);
        designRepository.save(design);

        return new AuthResponse(HttpStatus.CREATED.value(), "Design created successfully", null);
    }



    public AuthResponse updateDesign(Long designId, String designName, String designUrl, MultipartFile[] imageFiles) throws IOException {
        Optional<Design> optionalDesign = designRepository.findById(designId);
        if (optionalDesign.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Design not found with ID: " + designId, null);
        }

        Design design = optionalDesign.get();

        if (designName != null && !designName.isBlank()) {
            design.setDesignName(designName);
        }

        if (designUrl != null && !designUrl.isBlank()) {
            design.setDesignUrl(designUrl);
        }

        // Upload new images if any
        if (imageFiles != null && imageFiles.length > 0) {
            Path uploadPath = Paths.get(uploadImagePath).toAbsolutePath();
            Files.createDirectories(uploadPath);

            List<DesignImage> newImageList = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(filename);
                    Files.write(filePath, file.getBytes());

                    DesignImage image = new DesignImage();
                    image.setDesignUrl("/uploads/" + filename);
                    image.setDesign(design);
                    newImageList.add(image);
                }
            }

            design.getDesignImages().addAll(newImageList);
        }

        designRepository.save(design);
        return new AuthResponse(HttpStatus.OK.value(), "Design updated successfully", null);
    }



    public AuthResponse getAllDesigns() {
        List<Design> designs = designRepository.findAll();

        List<Map<String, Object>> result = designs.stream().map(design -> {
            Map<String, Object> map = new HashMap<>();
            map.put("designId", design.getDesignId());
            map.put("designName", design.getDesignName());
            map.put("designUrl", design.getDesignUrl());
            map.put("productId", design.getProduct().getProductId());
            map.put("images", design.getDesignImages().stream()
                    .map(img -> Map.of("imageId", img.getDesignImageId(), "url", img.getDesignUrl()))
                    .toList());
            return map;
        }).toList();

        return new AuthResponse(HttpStatus.OK.value(), "All designs fetched", result);
    }

    public AuthResponse getDesignById(Long designId) {
        Design design = designRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design not found with ID: " + designId));

        Map<String, Object> response = new HashMap<>();
        response.put("designId", design.getDesignId());
        response.put("designName", design.getDesignName());
        response.put("designUrl", design.getDesignUrl());
        response.put("productId", design.getProduct().getProductId());
        response.put("images", design.getDesignImages().stream()
                .map(img -> Map.of("imageId", img.getDesignImageId(), "url", img.getDesignUrl()))
                .toList());

        return new AuthResponse(HttpStatus.OK.value(), "Design found", response);
    }

    public AuthResponse deleteAllDesigns() {
        designRepository.deleteAll();
        return new AuthResponse(HttpStatus.OK.value(), "All designs deleted", null);
    }

    public AuthResponse deleteDesignById(Long designId) {
        if (!designRepository.existsById(designId)) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Design not found", null);
        }
        designRepository.deleteById(designId);
        return new AuthResponse(HttpStatus.OK.value(), "Design deleted", null);
    }



}
