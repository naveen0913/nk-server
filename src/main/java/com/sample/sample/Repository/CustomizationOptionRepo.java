package com.sample.sample.Repository;


import com.sample.sample.Model.CustomizationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomizationOptionRepo extends JpaRepository<CustomizationOption, Long> {}

