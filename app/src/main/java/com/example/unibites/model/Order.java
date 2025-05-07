package com.example.unibites.model;

public class Order {
    private String orderId;
    private String date;
    private String items;
    private double amount;
    private String status;
    private String imageResource; // Image resource for the first/main item

    public Order(String orderId, String date, String items, double amount, String status, String imageResource) {
        this.orderId = orderId;
        this.date = date;
        this.items = items;
        this.amount = amount;
        this.status = status;
        this.imageResource = imageResource;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getImageResource() {
        return imageResource;
    }
    
    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
} 