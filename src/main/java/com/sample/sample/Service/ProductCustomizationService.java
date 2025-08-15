package com.sample.sample.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.DTO.CustomizationOptionDTO;
import com.sample.sample.DTO.CustomizationThumbnailDTO;
import com.sample.sample.DTO.ProductCustomizationDTO;
import com.sample.sample.Model.*;
import com.sample.sample.Repository.*;
import com.sample.sample.Responses.AuthResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCustomizationService {

    private final ProductCustomizationRepo repo;
    private final ProductsRepository productsRepository;
    private final CustomOptionRepository customOptionRepository;
    private final CustomizationThumbnailsRepo customizationThumbnailsRepo;
    private final CustomizationImageRepo customizationImageRepo;
    private final HotspotRepo hotspotRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${file.upload-dir}")
    private String uploadPath;

    public ProductCustomizationService(ProductCustomizationRepo repo, ProductsRepository productsRepository, CustomOptionRepository customOptionRepository, CustomizationThumbnailsRepo customizationThumbnailsRepo, CustomizationImageRepo customizationImageRepo, HotspotRepo hotspotRepo) {
        this.repo = repo;
        this.productsRepository = productsRepository;
        this.customOptionRepository = customOptionRepository;
        this.customizationThumbnailsRepo = customizationThumbnailsRepo;
        this.customizationImageRepo = customizationImageRepo;
        this.hotspotRepo = hotspotRepo;
    }

    @Transactional
    public AuthResponse saveCustomization(Long productId, String dtoJson,
                                          MultipartFile bannerImage,
                                          List<MultipartFile> thumbnails) throws Exception {
        ProductCustomizationDTO dto = mapper.readValue(dtoJson, ProductCustomizationDTO.class);

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductCustomization entity = new ProductCustomization();
//        entity.setDescription(dto.getDescription());
        entity.setInput(dto.isInput());
        entity.setQuantity(dto.isQuantity());
        entity.setCart(dto.isCart());
        entity.setUpload(dto.isUpload());
        entity.setDesign(dto.isDesign());
        entity.setGiftWrap(dto.isGiftWrap());
        entity.setMultiUpload(dto.isMultiUpload());
        entity.setProduct(product);


        if (bannerImage != null && !bannerImage.isEmpty()) {
            String bannerUrl = saveFile(bannerImage);
            entity.setBannerImageUrl(bannerUrl);
        }


        List<CustomizationThumbnailUrls> thumbEntities = new ArrayList<>();
        if (thumbnails != null) {
            for (MultipartFile thumb : thumbnails) {
                if (thumb != null && !thumb.isEmpty()) {
                    String imageUrl = saveFile(thumb);

                    CustomizationThumbnailUrls thumbEntity = new CustomizationThumbnailUrls();
                    thumbEntity.setThumbnailUrl(imageUrl);
                    thumbEntity.setProductCustomization(entity);

                    thumbEntities.add(thumbEntity);
                }
            }
        }
        entity.setThumbnailImages(thumbEntities);


        List<CustomizationOption> optionEntities = new ArrayList<>();
        if (dto.getOptions() != null) {
            optionEntities = dto.getOptions().stream().map(opt -> {
                CustomizationOption co = new CustomizationOption();
                co.setOptionLabel(opt.getOptionLabel());
                co.setOriginalPrice(opt.getOriginalPrice());
                co.setOldPrice(opt.getOldPrice());
                co.setDiscount(opt.getDiscount());
                co.setMostPopular(opt.isMostPopular());
                co.setOptionSheetCount(opt.getOptionSheetCount());
                co.setCreatedTime(LocalDateTime.now());
                co.setProductCustomization(entity);

                return co;
            }).collect(Collectors.toList());
        }

        entity.setCustomizationOptions(optionEntities);

        repo.save(entity);
        return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
    }


    @Transactional
    public AuthResponse addProductCustomizationImage(Long productId, MultipartFile customFile, String hotspotsJson) throws IOException {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductCustomizationImage customizationImage = new ProductCustomizationImage();

        if (customFile != null && !customFile.isEmpty()) {
            String customImageUrl = saveFile(customFile);
            customizationImage.setCustomImage(customImageUrl);
        }
        customizationImage.setProduct(product);
        customizationImageRepo.save(customizationImage);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> hotspots =
                mapper.readValue(hotspotsJson, new TypeReference<List<Map<String, Object>>>() {
                });

        for (Map<String, Object> hotspotData : hotspots) {
            String shapeType = (String) hotspotData.get("shape");
            List<Map<String, Object>> points = (List<Map<String, Object>>) hotspotData.get("points");

            Hotspot hotspot = new Hotspot();
            hotspot.setShapeType(shapeType); // assuming setShapeType(String) exists
            hotspot.setProductCustomizationImage(customizationImage);
            // Store points as JSON string
            hotspot.setCoordinates(mapper.writeValueAsString(points));
            hotspotRepo.save(hotspot);
        }
        return new AuthResponse(HttpStatus.CREATED.value(), "created", null);
    }


    private String saveFile(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path directory = Paths.get("").toAbsolutePath().resolve(uploadPath);
        Files.createDirectories(directory);

        Path filePath = directory.resolve(filename);
        file.transferTo(filePath.toFile());

        return "/uploads/" + filename;
    }

    public AuthResponse getAllCustomizations() {

        List<Products> productList = productsRepository.findAll();
        if (productList.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "products not found", null);
        }
        return new AuthResponse(HttpStatus.OK.value(), "ok", productList);
    }

    public AuthResponse getCustomizationById(Long id) {
        Optional<Products> product = productsRepository.findById(id);

        if (product.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "product not found", null);
        }

        return new AuthResponse(HttpStatus.OK.value(), "ok", product.get());
    }


    @Transactional
    public AuthResponse updateCustomization(Long customizationId, String dtoJson,
                                            MultipartFile bannerImage,
                                            List<MultipartFile> thumbnails) throws Exception {

        if (dtoJson == null || dtoJson.isBlank()) {
            throw new IllegalArgumentException("jsonData is required for update.");
        }

        ProductCustomizationDTO dto = mapper.readValue(dtoJson, ProductCustomizationDTO.class);

        ProductCustomization entity = repo.findById(customizationId)
                .orElseThrow(() -> new RuntimeException("Customization not found"));


//        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        entity.setInput(dto.isInput());
        entity.setQuantity(dto.isQuantity());
        entity.setUpload(dto.isUpload());
        entity.setDesign(dto.isDesign());
        entity.setGiftWrap(dto.isGiftWrap());
        entity.setMultiUpload(dto.isMultiUpload());
        entity.setCart(dto.isCart());


        if (bannerImage != null && !bannerImage.isEmpty()) {
            String bannerUrl = saveFile(bannerImage);
            entity.setBannerImageUrl(bannerUrl);
        }


        List<CustomizationThumbnailUrls> thumbEntities = new ArrayList<>();
        if (thumbnails != null) {
            for (MultipartFile thumb : thumbnails) {
                if (thumb != null && !thumb.isEmpty()) {
                    String imageUrl = saveFile(thumb);

                    CustomizationThumbnailUrls thumbEntity = new CustomizationThumbnailUrls();
                    thumbEntity.setThumbnailUrl(imageUrl);
                    thumbEntity.setProductCustomization(entity);

                    thumbEntities.add(thumbEntity);
                }
            }
        }
        entity.setThumbnailImages(thumbEntities);


        List<CustomizationOption> optionEntities = new ArrayList<>();
        if (dto.getOptions() != null) {
            optionEntities = dto.getOptions().stream().map(opt -> {
                CustomizationOption co = new CustomizationOption();
                co.setOptionLabel(opt.getOptionLabel());
                co.setOriginalPrice(opt.getOriginalPrice());
                co.setOldPrice(opt.getOldPrice());
                co.setDiscount(opt.getDiscount());
                co.setMostPopular(opt.isMostPopular());
                co.setOptionSheetCount(opt.getOptionSheetCount());
                co.setProductCustomization(entity);
                return co;
            }).collect(Collectors.toList());
        }

        entity.setCustomizationOptions(optionEntities);

        repo.save(entity);

        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
    }


    @Transactional
    public AuthResponse updateCustomizationOptions(Long id, CustomizationOptionDTO optionDTO) {
        CustomizationOption customizationOption = customOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customization Option not found with ID: " + id));

        if (optionDTO.getOptionLabel() != null) {
            customizationOption.setOptionLabel(optionDTO.getOptionLabel());
        }
        if (optionDTO.getOriginalPrice() != null) {
            customizationOption.setOriginalPrice(optionDTO.getOriginalPrice());
        }
        if (optionDTO.getOldPrice() != null) {
            customizationOption.setOldPrice(optionDTO.getOldPrice());
        }
        if (optionDTO.getDiscount() != null) {
            customizationOption.setDiscount(optionDTO.getDiscount());
        }
        customizationOption.setMostPopular(optionDTO.isMostPopular());
        if (customizationOption.getOptionSheetCount() != null) {
            customizationOption.setOptionSheetCount(optionDTO.getOptionSheetCount());
        }
        customizationOption.setUpdatedTime(LocalDateTime.now());
        customOptionRepository.save(customizationOption);
        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
    }


    @Transactional
    public AuthResponse deleteCustomizationOptionById(Long customizationOptionId) {
        CustomizationOption customizationOption = customOptionRepository.findById(customizationOptionId)
                .orElseThrow(() -> new EntityNotFoundException("Customization Option not found"));

        customOptionRepository.deleteCustomizationOptionId(customizationOptionId);

        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
    }


    @Transactional
    public AuthResponse deleteCustomizationThumbnailUrl(Long customizationThumbnaiId) {
        CustomizationThumbnailUrls thumbnailUrls = customizationThumbnailsRepo.findById(customizationThumbnaiId)
                .orElseThrow(() -> new EntityNotFoundException("Customization Thumbnail Url Option not found"));

        customizationThumbnailsRepo.deleteCustomizationThumbnailUrl(customizationThumbnaiId);

        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
    }

    @Transactional
    public AuthResponse deleteAllCustomizationOptions(Long customizationId) {
        List<CustomizationOption> customizationOption = customOptionRepository.findAllByProductCustomizationId(customizationId);
        if (customizationOption.isEmpty()) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Option not found", null);
        }
        customOptionRepository.deleteAllOptionsByProductCustomizationId(customizationId);
        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
    }


    @Transactional
    public AuthResponse deleteCustomization(Long customizationId) {
        ProductCustomization customization = repo.findById(customizationId)
                .orElseThrow(() -> new EntityNotFoundException("Customization not found"));

        repo.deleteOptionsByCustomizationId(customizationId);
        repo.deleteCustomizationThumbnailsById(customizationId);
        repo.deleteCustomizationById(customizationId);

        return new AuthResponse(HttpStatus.OK.value(), "ok", null);
    }


}