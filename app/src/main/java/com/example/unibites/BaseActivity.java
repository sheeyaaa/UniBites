package com.example.unibites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupFooterNavigation() {
        // Find footer navigation buttons
        ImageButton navHome = findViewById(R.id.navHome);
        ImageButton navFood = findViewById(R.id.navFood);
        ImageButton navCart = findViewById(R.id.navCart);
        ImageButton navHistory = findViewById(R.id.navHistory);
        
        // Update cart badge
        updateCartBadge();
        
        // Set up navigation button click listeners
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                if (!(this instanceof HomeActivity)) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        
        if (navFood != null) {
            navFood.setOnClickListener(v -> {
                if (!(this instanceof FoodMenuActivity)) {
                    Intent intent = new Intent(this, FoodMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        
        if (navCart != null) {
            navCart.setOnClickListener(v -> {
                if (!(this instanceof CartActivity)) {
                    Intent intent = new Intent(this, CartActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        
        if (navHistory != null) {
            navHistory.setOnClickListener(v -> {
                if (!(this instanceof OrderHistoryActivity)) {
                    Intent intent = new Intent(this, OrderHistoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
    
    // Add method to update cart badge
    protected void updateCartBadge() {
        try {
            // Get the badge view
            TextView cartBadge = findViewById(R.id.cartBadge);
            if (cartBadge != null) {
                // Get cart item count from CartManager
                int itemCount = com.example.unibites.utils.CartManager.getInstance().getTotalQuantity();
                
                // Show badge if there are items in cart
                if (itemCount > 0) {
                    cartBadge.setVisibility(View.VISIBLE);
                    cartBadge.setText(String.valueOf(itemCount));
                } else {
                    cartBadge.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            Log.e("BaseActivity", "Error updating cart badge", e);
        }
    }
} 