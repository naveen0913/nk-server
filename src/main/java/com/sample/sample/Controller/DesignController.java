package com.sample.sample.Controller;


import com.sample.sample.Model.Design;
import com.sample.sample.Responses.AuthResponse;
import com.sample.sample.Service.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/designs")
@CrossOrigin(origins = "*") // Optional: Enable for frontend access
public class DesignController {

    @Autowired
    private DesignService designService;

    @PostMapping
    public AuthResponse addDesign(@RequestBody Design design) {
        return designService.addDesign(design);
    }

    @PutMapping("/{id}")
    public AuthResponse updateDesign(@PathVariable Long id, @RequestBody Design design) {
        return designService.updateDesign(id, design);
    }

    @DeleteMapping("/{id}")
    public AuthResponse deleteDesign(@PathVariable Long id) {
        return designService.deleteDesign(id);
    }

    @GetMapping
    public AuthResponse getAllDesigns() {
        return designService.getAllDesigns();
    }

    @GetMapping("/{id}")
    public AuthResponse getDesignById(@PathVariable Long id) {
        return designService.getDesignById(id);
    }
}

