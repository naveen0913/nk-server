package com.sample.sample.Repository;

import com.sample.sample.Model.ProductCustomization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCustomizationRepo extends JpaRepository<ProductCustomization, Long> {

}
