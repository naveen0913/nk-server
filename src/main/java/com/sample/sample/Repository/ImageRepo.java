package com.sample.sample.Repository;

import com.sample.sample.Model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<Images, Long> {
}
