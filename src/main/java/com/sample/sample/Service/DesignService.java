package com.sample.sample.Service;


import com.sample.sample.Model.Design;
import com.sample.sample.Repository.DesignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DesignService {

    @Autowired
    private DesignRepo designRepo;

    public Design addDesign(Design design) {
        return designRepo.save(design);
    }

    public Design updateDesign(Long id, Design updatedDesign) {
        Optional<Design> optionalDesign = designRepo.findById(id);
        if (optionalDesign.isPresent()) {
            Design design = optionalDesign.get();
            design.setName(updatedDesign.getName());
            return designRepo.save(design);
        } else {
            throw new RuntimeException("Design not found with id: " + id);
        }
    }

    public void deleteDesign(Long id) {
        designRepo.deleteById(id);
    }

    public List<Design> getAllDesigns() {
        return designRepo.findAll();
    }

    public Optional<Design> getDesignById(Long id) {
        return designRepo.findById(id);
    }
}
