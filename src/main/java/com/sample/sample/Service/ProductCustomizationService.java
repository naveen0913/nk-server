package com.sample.sample.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.DTO.ProductCustomizationDTO;
import com.sample.sample.Model.CustomizationOption;
import com.sample.sample.Model.ProductCustomization;
import com.sample.sample.Model.Products;
import com.sample.sample.Repository.ProductCustomizationRepo;
import com.sample.sample.Repository.ProductsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final ProductsRepository productsRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${file.upload-dir}")
    private String uploadPath;

    public ProductCustomizationService(ProductCustomizationRepo repo, ProductsRepository productsRepository) {
        this.repo = repo;
        this.productsRepository = productsRepository;
    }

    @Transactional
    public ProductCustomization saveCustomization( Long productId, String dtoJson,
                                                  MultipartFile bannerImage,
                                                  List<MultipartFile> thumbnails) throws Exception {
        ProductCustomizationDTO dto = mapper.readValue(dtoJson, ProductCustomizationDTO.class);

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductCustomization entity = new ProductCustomization();
        entity.setDescription(dto.getDescription());
        entity.setInput(dto.isInput());
        entity.setQuantity(dto.isQuantity());
        entity.setCart(dto.isCart());
        entity.setUpload(dto.isUpload());
        entity.setDesign(dto.isDesign());
        entity.setGiftWrap(dto.isGiftWrap());
        entity.setMultiUpload(dto.isMultiUpload());
        entity.setProduct(product);

        // Save banner image
        if (bannerImage != null && !bannerImage.isEmpty()) {
            String bannerUrl = saveFile(bannerImage);
            entity.setBannerImageUrl(bannerUrl);
        }

        // Save thumbnails
        List<String> thumbUrls = new ArrayList<>();
        if (thumbnails != null) {
            for (MultipartFile thumb : thumbnails) {
                if (thumb != null && !thumb.isEmpty()) {
                    thumbUrls.add(saveFile(thumb));
                }
            }
        }
        entity.setThumbnailImageUrls(thumbUrls);

        // Save customization options
        List<CustomizationOption> optionEntities = new ArrayList<>();
        if (dto.getOptions() != null) {
            optionEntities = dto.getOptions().stream().map(opt -> {
                CustomizationOption co = new CustomizationOption();
                co.setOptionLabel(opt.getOptionLabel());
                co.setOriginalPrice(opt.getOriginalPrice());
                co.setOldPrice(opt.getOldPrice());
                co.setDiscount(opt.getDiscount());
                co.setMostPopular(opt.isMostPopular());
                co.setProductCustomization(entity);
                return co;
            }).collect(Collectors.toList());
        }

        entity.setCustomizationOptions(optionEntities);

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

    @Transactional
    public ProductCustomization updateCustomization(Long customizationId, String dtoJson,
                                                    MultipartFile bannerImage,
                                                    List<MultipartFile> thumbnails) throws Exception {

        if (dtoJson == null || dtoJson.isBlank()) {
            throw new IllegalArgumentException("jsonData is required for update.");
        }

        ProductCustomizationDTO dto = mapper.readValue(dtoJson, ProductCustomizationDTO.class);

        ProductCustomization entity = repo.findById(customizationId)
                .orElseThrow(() -> new RuntimeException("Customization not found"));

        // Update basic fields
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        entity.setInput(dto.isInput());
        entity.setQuantity(dto.isQuantity());
        entity.setUpload(dto.isUpload());
        entity.setDesign(dto.isDesign());
        entity.setGiftWrap(dto.isGiftWrap());
        entity.setMultiUpload(dto.isMultiUpload());

        // Update banner image only if a new file is sent
        if (bannerImage != null && !bannerImage.isEmpty()) {
            String bannerUrl = saveFile(bannerImage);
            entity.setBannerImageUrl(bannerUrl);
        }

        // Update thumbnails only if list is present and has at least one valid file
        if (thumbnails != null) {
            List<String> thumbUrls = new ArrayList<>();

            for (MultipartFile thumb : thumbnails) {
                if (thumb != null && !thumb.isEmpty()) {
                    try {
                        String url = saveFile(thumb);
                        thumbUrls.add(url);
                    } catch (IOException e) {
                        // Optionally log and skip the failed thumbnail
                        System.err.println("Failed to save thumbnail: " + e.getMessage());
                        // Optionally: throw new RuntimeException("Thumbnail upload failed", e);
                    }
                }
            }

            if (!thumbUrls.isEmpty()) {
                entity.setThumbnailImageUrls(thumbUrls); // Replace old thumbnails
            }
        }


        // Replace customization options if present
        if (dto.getOptions() != null) {
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

            entity.setCustomizationOptions(optionEntities);
        }

        return repo.save(entity);
    }


    @Transactional
    public void deleteCustomization(Long customizationId) {
        ProductCustomization customization = repo.findById(customizationId)
                .orElseThrow(() -> new EntityNotFoundException("Customization not found"));

        // Delete child tables first
        repo.deleteOptionsByCustomizationId(customizationId);
        repo.deleteCustomizationThumbnailsById(customizationId);
        repo.deleteCustomizationById(customizationId);
    }



}