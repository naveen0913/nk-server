package com.sample.sample.Repository;

import com.sample.sample.Model.Design;
import com.sample.sample.Model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
    public interface DesignRepo extends JpaRepository<Design, Long> {

    Design findByProduct(Products product);

//    List<Design> findAll();
//    Optional<Design> findById(Long id);

}
