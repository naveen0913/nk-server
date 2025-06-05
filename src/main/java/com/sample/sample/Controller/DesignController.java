package com.sample.sample.Controller;


import com.sample.sample.Model.Design;
import com.sample.sample.Service.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designs")
@CrossOrigin(origins = "*")
public class DesignController {

    @Autowired
    private DesignService designService;

    @GetMapping
    public List<Design> getAllDesigns() {
        return designService.getAllDesigns();
    }

    @PostMapping
    public Design addDesign(@RequestBody Design design) {
        return designService.addDesign(design);
    }
}
