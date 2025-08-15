package com.sample.sample.Repository;

import com.sample.sample.Model.Hotspot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotspotRepo extends JpaRepository<Hotspot,Long> {

    @Query(value = "select * from hotspot where product_customization_image_id = :productImageId",nativeQuery = true)
    List<Hotspot> findByProductCustomImageId(@Param("productImageId") Long productImageId);

}
