package com.sample.sample.Controller;



import com.sample.sample.DTO.DashboardResponse;
import com.sample.sample.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboardData() {
        long totalProducts = dashboardService.getTotalProducts();
        long totalUsers = dashboardService.getTotalUsers();
        return new DashboardResponse(totalProducts, totalUsers);
    }
}
