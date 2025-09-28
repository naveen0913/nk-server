//package com.sample.sample.Controller;
//
//
//
//import com.sample.sample.DTO.DashboardResponse;
//import com.sample.sample.Service.DashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/dashboard")
//public class DashboardController {
//
//    @Autowired
//    private DashboardService dashboardService;
//
//    @GetMapping
//    public DashboardResponse getDashboardData() {
//        long totalProducts = dashboardService.getTotalProducts();
//        long totalUsers = dashboardService.getTotalUsers();
//        long totalOrders = dashboardService.getTotalOrders();
//        long totalCartItems = dashboardService.getTotalCartItems();
//        long totalPayments = dashboardService.getTotalPayments();
//        long totalAddresses = dashboardService.getTotalAddresses();
//        long todayOrders = dashboardService.getCurrentDateOrders();
//        long todayPayments = dashboardService.getTodayPayments();
//        return new DashboardResponse(totalProducts, totalUsers,totalOrders,totalPayments,totalCartItems,totalAddresses,todayOrders,todayPayments);
//    }
//}
