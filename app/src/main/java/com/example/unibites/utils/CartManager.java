package com.example.unibites.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.unibites.model.CartItem;
import com.example.unibites.model.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton cart manager to handle cart operations across the app
 */
public class CartManager {
    private static final String TAG = "CartManager";
    
    // Singleton instance
    private static CartManager instance;
    
    // Cart items map with itemId as key for quick lookup
    private final Map<String, CartItem> cartItemsMap;
    // List for ordered display
    private final List<CartItem> cartItemsList;
    
    // Listener interface for cart updates
    public interface CartUpdateListener {
        void onCartUpdated(List<CartItem> cartItems);
    }
    
    // List of registered listeners
    private final List<CartUpdateListener> listeners = new ArrayList<>();
    
    // Private constructor
    private CartManager() {
        cartItemsMap = new HashMap<>();
        cartItemsList = new ArrayList<>();
        Log.d(TAG, "CartManager initialized");
    }
    
    // Get singleton instance
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }
    
    // Register listener for cart updates
    public void registerListener(CartUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    // Unregister listener
    public void unregisterListener(CartUpdateListener listener) {
        listeners.remove(listener);
    }
    
    // Notify all listeners about cart updates
    private void notifyListeners() {
        for (CartUpdateListener listener : listeners) {
            listener.onCartUpdated(getCartItems());
        }
    }
    
    // Add item to cart
    public void addToCart(MenuItem menuItem, Context context) {
        String itemId = menuItem.getItemId();
        
        if (cartItemsMap.containsKey(itemId)) {
            // Item already in cart, increase quantity
            CartItem existingItem = cartItemsMap.get(itemId);
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            Log.d(TAG, "Increased quantity for " + existingItem.getName() + " to " + existingItem.getQuantity());
            
            if (context != null) {
                Toast.makeText(context, existingItem.getName() + " quantity increased", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new item to cart with quantity 1
            CartItem newItem = menuItem.toCartItem();
            newItem.setQuantity(1);
            cartItemsMap.put(itemId, newItem);
            cartItemsList.add(newItem);
            Log.d(TAG, "Added new item to cart: " + newItem.getName());
            
            if (context != null) {
                Toast.makeText(context, newItem.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            }
        }
        
        notifyListeners();
    }
    
    // Increase item quantity
    public void increaseQuantity(String itemId) {
        if (cartItemsMap.containsKey(itemId)) {
            CartItem item = cartItemsMap.get(itemId);
            item.setQuantity(item.getQuantity() + 1);
            notifyListeners();
        }
    }
    
    // Decrease item quantity
    public void decreaseQuantity(String itemId) {
        if (cartItemsMap.containsKey(itemId)) {
            CartItem item = cartItemsMap.get(itemId);
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyListeners();
            } else {
                // Remove item if quantity becomes 0
                removeItem(itemId);
            }
        }
    }
    
    // Remove item from cart
    public void removeItem(String itemId) {
        if (cartItemsMap.containsKey(itemId)) {
            CartItem item = cartItemsMap.get(itemId);
            cartItemsList.remove(item);
            cartItemsMap.remove(itemId);
            notifyListeners();
            Log.d(TAG, "Removed item from cart: " + item.getName());
        }
    }
    
    // Clear the entire cart
    public void clearCart() {
        cartItemsMap.clear();
        cartItemsList.clear();
        notifyListeners();
        Log.d(TAG, "Cart cleared");
    }
    
    // Get all cart items
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItemsList);
    }
    
    // Get cart item count
    public int getItemCount() {
        return cartItemsList.size();
    }
    
    // Get total quantity of all items
    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (CartItem item : cartItemsList) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }
    
    // Get total cart value
    public double getTotalCartValue() {
        double total = 0;
        for (CartItem item : cartItemsList) {
            total += item.getTotalPrice();
        }
        return total;
    }
    
    // Add multiple items to cart (for reordering)
    public void reorder(List<CartItem> items, Context context) {
        for (CartItem item : items) {
            // First check if item exists
            if (cartItemsMap.containsKey(item.getItemId())) {
                // Increase quantity
                CartItem existingItem = cartItemsMap.get(item.getItemId());
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            } else {
                // Add new item
                cartItemsMap.put(item.getItemId(), item);
                cartItemsList.add(item);
            }
        }
        
        notifyListeners();
        if (context != null) {
            Toast.makeText(context, "Items have been added to your cart", Toast.LENGTH_SHORT).show();
        }
    }
} 