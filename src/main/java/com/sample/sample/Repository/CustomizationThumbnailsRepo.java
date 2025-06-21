package com.sample.sample.Repository;

import com.sample.sample.Model.CustomizationThumbnailUrls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizationThumbnailsRepo extends JpaRepository<CustomizationThumbnailUrls,Long> {

    @Modifying
    @Query("DELETE FROM CustomizationThumbnailUrls c WHERE c.thumbnailId = :customizationThumbnailId")
    void deleteCustomizationThumbnailUrl(@Param("customizationThumbnailId") Long customizationThumnailId);

}
