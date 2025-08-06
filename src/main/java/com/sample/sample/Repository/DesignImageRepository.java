package com.sample.sample.Repository;



import com.sample.sample.Model.DesignImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignImageRepository extends JpaRepository<DesignImage, Long> {
    // Optional: fetch all images by design
    // List<DesignImage> findByDesign_DesignId(Long designId);
}
