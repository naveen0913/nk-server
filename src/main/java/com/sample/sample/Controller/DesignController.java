package com.sample.sample.Controller;




import com.sample.sample.Model.Design;
import com.sample.sample.Service.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/designs")
@CrossOrigin(origins = "*") // Optional: Enable for frontend access
public class DesignController {

    @Autowired
    private DesignService designService;

    @PostMapping
    public Design addDesign(@RequestBody Design design) {
        return designService.addDesign(design);
    }

    @PutMapping("/{id}")
    public Design updateDesign(@PathVariable Long id, @RequestBody Design design) {
        return designService.updateDesign(id, design);
    }

    @DeleteMapping("/{id}")
    public void deleteDesign(@PathVariable Long id) {
        designService.deleteDesign(id);
    }

    @GetMapping
    public List<Design> getAllDesigns() {
        return designService.getAllDesigns();
    }

    @GetMapping("/{id}")
    public Optional<Design> getDesignById(@PathVariable Long id) {
        return designService.getDesignById(id);
    }
}
