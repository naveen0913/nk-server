package com.sample.sample.Repository;

import com.sample.sample.Model.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImagesRepo extends JpaRepository<ProductImages,Long> {

}
