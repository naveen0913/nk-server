package com.sample.sample.Controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.DTO.ProductCustomizationDTO;
import com.sample.sample.Model.ProductCustomization;
import com.sample.sample.Model.CustomizationOption;
import com.sample.sample.Service.ProductCustomizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product-customizations")
public class ProductCustomizationController {

    @Autowired
    private ProductCustomizationService service;

    @PostMapping(consumes = "multipart/form-data")
    public ProductCustomization createProductCustomization(
            @RequestPart("bannerImage") MultipartFile bannerImage,
            @RequestPart("thumbnailImages") List<MultipartFile> thumbnailImages,
            @RequestPart("customizationData") String customizationDataJson
    ) throws IOException {

        // Simulate file storage and URLs
        String bannerImageUrl = "uploads/" + bannerImage.getOriginalFilename();
        List<String> thumbnailUrls = new ArrayList<>();
        for (MultipartFile file : thumbnailImages) {
            thumbnailUrls.add("uploads/" + file.getOriginalFilename());
        }

        // Convert JSON to DTO
        ObjectMapper objectMapper = new ObjectMapper();
        ProductCustomizationDTO dto = objectMapper.readValue(customizationDataJson, ProductCustomizationDTO.class);

        // Build entity
        ProductCustomization customization = new ProductCustomization();
        customization.setBannerImageUrl(bannerImageUrl);
        customization.setThumbnailImageUrls(thumbnailUrls);
        customization.setInput(dto.isInput());
        customization.setQuantity(dto.isQuantity());
        customization.setCart(dto.isCart());
        customization.setUpload(dto.isUpload());
        customization.setDesign(dto.isDesign());
        customization.setGiftWrap(dto.isGiftWrap());

        for (CustomizationOption option : dto.getOptions()) {
            option.setProductCustomization(customization);
        }

        customization.setOptions(dto.getOptions());

        return service.save(customization);
    }

    @GetMapping
    public List<ProductCustomization> getAll() {
        return service.getAll();
    }
}
