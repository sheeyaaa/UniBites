package com.example.unibites.model;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private String itemId;
    private String name;
    private double price;
    private int quantity;
    private String imageUrl;
    private int imageResource; // Added for drawable resource IDs
    private String description;
    private String category;
    
    // Empty constructor needed for Firestore
    public MenuItem() {
        this.quantity = 0; // Default quantity is 0 until added to cart
    }
    
    public MenuItem(String itemId, String name, double price, String imageUrl, String description, String category) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = 0; // Default quantity is 0 until added to cart
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
    }

    // Constructor for using drawable resource IDs
    public MenuItem(String itemId, String name, double price, int imageResource) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = 0; // Default quantity is 0 until added to cart
        this.imageResource = imageResource;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getImageResource() {
        return imageResource;
    }
    
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    // Convert MenuItem to CartItem
    public CartItem toCartItem() {
        // If we have an imageUrl, use it; otherwise create CartItem with imageResource
        if (imageUrl != null && !imageUrl.isEmpty()) {
            return new CartItem(itemId, name, price, quantity, imageUrl);
        } else {
            return new CartItem(itemId, name, price, quantity, imageResource);
        }
    }
} 