package com.sample.sample.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.DTO.ProductCustomizationDTO;
import com.sample.sample.Model.CustomizationOption;
import com.sample.sample.Model.ProductCustomization;
import com.sample.sample.Repository.ProductCustomizationRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCustomizationService {

    private final ProductCustomizationRepo repo;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${upload.path:uploads}")
    private String uploadPath;


    public ProductCustomizationService(ProductCustomizationRepo repo) {
        this.repo = repo;
    }

    public ProductCustomization saveCustomization(String dtoJson,
                                                  MultipartFile bannerImage,
                                                  List<MultipartFile> thumbnails) throws Exception {
        ProductCustomizationDTO dto = mapper.readValue(dtoJson, ProductCustomizationDTO.class);

        ProductCustomization entity = new ProductCustomization();

        entity.setDescription(dto.getDescription());
        entity.setInput(dto.isInput());
        entity.setQuantity(dto.isQuantity());
        entity.setCart(dto.isCart());
        entity.setUpload(dto.isUpload());
        entity.setDesign(dto.isDesign());
        entity.setGiftWrap(dto.isGiftWrap());

        // Save banner
        String bannerUrl = saveFile(bannerImage);
        entity.setBannerImageUrl(bannerUrl);

        // Save thumbnails
        List<String> thumbUrls = new ArrayList<>();
        for (MultipartFile thumb : thumbnails) {
            thumbUrls.add(saveFile(thumb));
        }
        entity.setThumbnailImageUrls(thumbUrls);

        // Save options
        List<CustomizationOption> optionEntities = dto.getOptions().stream().map(opt -> {
            CustomizationOption co = new CustomizationOption();
            co.setOptionLabel(opt.getOptionLabel());
            co.setOriginalPrice(opt.getOriginalPrice());
            co.setOldPrice(opt.getOldPrice());
            co.setDiscount(opt.getDiscount());
            co.setMostPopular(opt.isMostPopular());
            co.setProductCustomization(entity);

            return co;
        }).collect(Collectors.toList());

        entity.setOptions(optionEntities);

        return repo.save(entity);
    }

    private String saveFile(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Resolve path relative to project root
        Path directory = Paths.get("").toAbsolutePath().resolve(uploadPath);
        Files.createDirectories(directory); // ensure it exists

        Path filePath = directory.resolve(filename);
        file.transferTo(filePath.toFile());

        // Return URL-like path for storing in DB
        return "/uploads/" + filename;
    }


    public List<ProductCustomization> getAllCustomizations() {
        return repo.findAll();
    }

    public Optional<ProductCustomization> getCustomizationById(Long id) {
        return repo.findById(id);
    }


}