package com.sample.sample.DTO;



public class DashboardResponse {
    private long totalProducts;
    private long totalUsers;
    private long totalOrders;
    private long totalPayments;
    private long totalCartItems;
    private long totalAddresses;
    private long totalDesigns;

    public DashboardResponse(long totalProducts, long totalUsers, long totalOrders, long totalPayments, long totalCartItems,long totalAddresses,long totalDesigns) {
        this.totalProducts = totalProducts;
        this.totalUsers = totalUsers;
        this.totalOrders = totalOrders;
        this.totalPayments = totalPayments;
        this.totalCartItems = totalCartItems;
        this.totalAddresses = totalAddresses;
        this.totalDesigns = totalDesigns;
    }

    public long getTotalDesigns() {
        return totalDesigns;
    }

    public void setTotalDesigns(long totalDesigns) {
        this.totalDesigns = totalDesigns;
    }

    public long getTotalAddresses() {
        return totalAddresses;
    }

    public void setTotalAddresses(long totalAddresses) {
        this.totalAddresses = totalAddresses;
    }

    public long getTotalCartItems() {
        return totalCartItems;
    }

    public void setTotalCartItems(long totalCartItems) {
        this.totalCartItems = totalCartItems;
    }

    public long getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(long totalPayments) {
        this.totalPayments = totalPayments;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
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

