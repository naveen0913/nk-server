package com.sample.sample.Repository;

import com.sample.sample.Model.CustomizationOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomOptionRepository extends JpaRepository<CustomizationOption,Long> {

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM CustomizationOption c WHERE c.productCustomization.id = :customizationId")
//    void deleteAllByProductCustomizationId(@Param("customizationId") Long customizationId);

}
