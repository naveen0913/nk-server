package com.sample.sample.Repository;

import com.sample.sample.Model.CustomizationOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomOptionRepository extends JpaRepository<CustomizationOption,Long> {


    @Modifying
    @Query("DELETE FROM CustomizationOption c WHERE c.id = :customizationId")
    void deleteCustomizationOptionId(@Param("customizationId") Long customizationId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomizationOption c WHERE c.productCustomization.id = :customizationId")
    void deleteAllOptionsByProductCustomizationId(@Param("customizationId") Long customizationId);

    List<CustomizationOption> findAllByProductCustomizationId(Long customizationId);

}
