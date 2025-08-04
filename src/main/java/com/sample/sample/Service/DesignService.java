package com.sample.sample.Service;


import com.sample.sample.Model.Design;
import com.sample.sample.Repository.DesignRepo;
import com.sample.sample.Responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DesignService {

    @Autowired
    private DesignRepo designRepo;

    public AuthResponse addDesign(Design design) {
        Design saved = designRepo.save(design);
        return new AuthResponse(HttpStatus.CREATED.value(), "Design added successfully", saved);
    }

    public AuthResponse updateDesign(Long id, Design updatedDesign) {
        Optional<Design> optionalDesign = designRepo.findById(id);
        if (optionalDesign.isPresent()) {
            Design design = optionalDesign.get();
            design.setName(updatedDesign.getName());
            Design updated = designRepo.save(design);
            return new AuthResponse(HttpStatus.OK.value(), "Design updated successfully", updated);
        } else {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Design not found with ID: " + id, null);
        }
    }

    public AuthResponse deleteDesign(Long id) {
        if (!designRepo.existsById(id)) {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Design not found with ID: " + id, null);
        }
        designRepo.deleteById(id);
        return new AuthResponse(HttpStatus.OK.value(), "Design deleted successfully", null);
    }

    public AuthResponse getAllDesigns() {
        List<Design> designs = designRepo.findAll();
        return new AuthResponse(HttpStatus.OK.value(), "All designs fetched", designs);
    }

    public AuthResponse getDesignById(Long id) {
        Optional<Design> design = designRepo.findById(id);
        if (design.isPresent()) {
            return new AuthResponse(HttpStatus.OK.value(), "Design fetched successfully", design.get());
        } else {
            return new AuthResponse(HttpStatus.NOT_FOUND.value(), "Design not found with ID: " + id, null);
        }
    }

}
