package com.sample.sample.Service;



import com.sample.sample.Model.ProductCustomization;
import com.sample.sample.Repository.ProductCustomizationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCustomizationService {

    @Autowired
    private ProductCustomizationRepo repo;

    public List<ProductCustomization> getAll() {
        return repo.findAll();
    }

    public ProductCustomization save(ProductCustomization customization) {
        return repo.save(customization);
    }
}
