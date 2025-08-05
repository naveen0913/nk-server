package com.sample.sample.Repository;


import com.sample.sample.Model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    Optional<Products> findByProductIdAndProductStatus(Long productId, productStatus status);

}


