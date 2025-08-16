package com.sample.sample.Repository;

import com.sample.sample.Model.CustomerPreviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPreviewImageRepo extends JpaRepository<CustomerPreviewImage,Long> {
}
