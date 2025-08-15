package com.sample.sample.Repository;

import com.sample.sample.Model.ProductCustomizationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomizationImageRepo extends JpaRepository<ProductCustomizationImage,Long> {

    @Query(value = "select * from product_customization_image where product_id=:productId",nativeQuery = true)
    List<ProductCustomizationImage> findAllByProduct_Id(@Param("productId") Long productId);

}

