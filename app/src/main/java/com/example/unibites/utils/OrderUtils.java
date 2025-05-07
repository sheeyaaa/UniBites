package com.example.unibites.utils;

import android.util.Log;

import com.example.unibites.model.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for order-related operations
 */
public class OrderUtils {
    private static final String TAG = "OrderUtils";
    
    /**
     * Parse order items string into a list of CartItems
     * Example format: "2x Chole Bhature, 1x Tea"
     */
    public static List<CartItem> parseOrderItems(String orderItemsStr, String orderId) {
        List<CartItem> items = new ArrayList<>();
        
        if (orderItemsStr == null || orderItemsStr.isEmpty()) {
            return items;
        }
        
        // Split by comma to get individual items
        String[] itemsArray = orderItemsStr.split(",");
        
        // Pattern to match quantity and name: "2x Chole Bhature"
        Pattern pattern = Pattern.compile("(\\d+)x\\s+(.+)");
        
        for (String itemStr : itemsArray) {
            try {
                Matcher matcher = pattern.matcher(itemStr.trim());
                if (matcher.find()) {
                    int quantity = Integer.parseInt(matcher.group(1));
                    String name = matcher.group(2).trim();
                    
                    // Generate a unique id based on order ID and item name
                    String itemId = orderId + "_" + name.toLowerCase().replace(" ", "_");
                    
                    // Use default price since we don't have it in the order history
                    // In a real app, you would use a database lookup
                    double price = getDefaultPrice(name);
                    
                    // Create cart item
                    CartItem item = new CartItem(itemId, name, price, quantity, "");
                    items.add(item);
                    
                    Log.d(TAG, "Parsed order item: " + quantity + "x " + name);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing item: " + itemStr, e);
            }
        }
        
        return items;
    }
    
    /**
     * Get a default price for an item based on its name
     * In a real app, this would be replaced with a database lookup
     */
    private static double getDefaultPrice(String itemName) {
        // Common items with prices (hardcoded for simplicity)
        if (itemName.contains("Chole Bhature")) return 150.0;
        if (itemName.contains("Masala Dosa")) return 120.0;
        if (itemName.contains("Tea")) return 20.0;
        if (itemName.contains("Coffee")) return 35.0;
        if (itemName.contains("Cold Coffee")) return 80.0;
        if (itemName.contains("Chocolate") && itemName.contains("Shake")) return 60.0;
        if (itemName.contains("Samosa")) return 25.0;
        if (itemName.contains("Aloo Paratha")) return 85.0;
        if (itemName.contains("Rajma Chawal")) return 120.0;
        if (itemName.contains("Paneer")) return 160.0;
        
        // Default price if none match
        return 100.0;
    }
} 