package com.sample.sample.Repository;

import com.sample.sample.Model.ProductCustomization;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCustomizationRepo extends JpaRepository<ProductCustomization, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM customization_option WHERE product_customization_id = :customizationId", nativeQuery = true)
    void deleteOptionsByCustomizationId(@Param("customizationId") Long customizationId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product_customization WHERE id = :customizationId", nativeQuery = true)
    void deleteCustomizationById(@Param("customizationId") Long customizationId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product_customization_thumbnail_image_urls WHERE product_customization_id = :customizationId", nativeQuery = true)
    void deleteCustomizationThumbnailsById(@Param("customizationId") Long customizationId);
}
