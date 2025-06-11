package com.sample.sample.DTO;



public class DashboardResponse {
    private long totalProducts;
    private long totalUsers;

    public DashboardResponse(long totalProducts, long totalUsers) {
        this.totalProducts = totalProducts;
        this.totalUsers = totalUsers;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }
}

