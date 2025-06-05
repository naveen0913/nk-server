package com.sample.sample.Service;

import com.sample.sample.Model.Design;
import com.sample.sample.Repository.DesignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignService {

    @Autowired
    private DesignRepo designRepo;

    public List<Design> getAllDesigns() {
        return designRepo.findAll();
    }

    public Design addDesign(Design design) {
        return designRepo.save(design);
    }
}
