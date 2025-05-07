package com.example.unibites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

import com.example.unibites.model.CartItem;
import com.example.unibites.model.MenuItem;
import com.example.unibites.utils.CartManager;

import java.util.Calendar;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    
    private CartManager mCartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // Get CartManager instance
        mCartManager = CartManager.getInstance();
        
        // Setup the greeting message based on time of day
        setupGreeting();
        
        // Setup UI components
        setupUI();
        
        // The bottom navigation is now part of the layout
        // so we don't need to call setupFooterNavigation()
    }
    
    private void setupGreeting() {
        TextView greetingTextView = findViewById(R.id.greetingTextView);
        if (greetingTextView != null) {
            Calendar c = Calendar.getInstance();
            int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
            
            String greeting;
            if (timeOfDay >= 0 && timeOfDay < 12) {
                greeting = "Good Morning !";
            } else if (timeOfDay >= 12 && timeOfDay < 16) {
                greeting = "Good Afternoon !";
            } else if (timeOfDay >= 16 && timeOfDay < 21) {
                greeting = "Good Evening !";
            } else {
                greeting = "Good Night !";
            }
            
            greetingTextView.setText(greeting);
        }
    }
    
    private void setupUI() {
        // Setup menu button
        ImageButton menuButton = findViewById(R.id.menuButton);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show();
                // Implement menu functionality
            });
        }
        
        // Setup notification button
        ImageButton notificationButton = findViewById(R.id.notificationButton);
        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, UnderDevelopmentActivity.class);
                intent.putExtra("activity_name", "Notifications");
                startActivity(intent);
            });
        }
        
        // Setup bottom navigation
        setupBottomNavigation();
        
        // Setup Add More button
        ImageButton addMoreButton = findViewById(R.id.addMoreButton);
        if (addMoreButton != null) {
            addMoreButton.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, FoodMenuActivity.class);
                startActivity(intent);
            });
        }
        
        // Find all "Add To Cart" buttons and set their click listeners
        setupAddToCartButtons();
    }
    
    private void setupBottomNavigation() {
        ImageButton navHome = findViewById(R.id.navHome);
        ImageButton navFood = findViewById(R.id.navFood);
        ImageButton navCart = findViewById(R.id.navCart);
        ImageButton navHistory = findViewById(R.id.navHistory);
        
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                // Already on home screen, do nothing
            });
        }
        
        if (navFood != null) {
            navFood.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, FoodMenuActivity.class);
                startActivity(intent);
            });
        }
        
        if (navCart != null) {
            navCart.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }
        
        if (navHistory != null) {
            navHistory.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            });
        }
    }
    
    private void setupAddToCartButtons() {
        // Find all buttons with the text "Add To Cart"
        ScrollView scrollView = findViewById(R.id.contentScrollView);
        if (scrollView != null && scrollView.getChildCount() > 0) {
            View contentView = scrollView.getChildAt(0);
            if (contentView instanceof LinearLayout) {
                LinearLayout mainLayout = (LinearLayout) contentView;
                findAndSetupAddToCartButtons(mainLayout);
            }
        } else {
            Log.d(TAG, "ScrollView not found or empty");
        }
    }
    
    private void findAndSetupAddToCartButtons(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;
            if (button.getText().toString().equals("Add To Cart")) {
                button.setOnClickListener(v -> {
                    // Find the parent CardView to determine which item was clicked
                    View parent = (View) button.getParent();
                    while (parent != null && !(parent instanceof CardView)) {
                        parent = (View) parent.getParent();
                    }
                    
                    if (parent != null) {
                        // Find the item name and create a MenuItem
                        String itemName = "Item";
                        double itemPrice = 0.0;
                        String itemId = "home_item";
                        int imageResource = R.drawable.image;
                        
                        // Find the item details in the card
                        try {
                            // Look for TextView with item name
                            ViewGroup itemLayout = (ViewGroup) parent;
                            for (int i = 0; i < itemLayout.getChildCount(); i++) {
                                View child = itemLayout.getChildAt(i);
                                if (child instanceof TextView) {
                                    TextView textView = (TextView) child;
                                    // Assume the first TextView is the name
                                    itemName = textView.getText().toString();
                                    break;
                                } else if (child instanceof ViewGroup) {
                                    // Look in nested ViewGroups
                                    for (int j = 0; j < ((ViewGroup) child).getChildCount(); j++) {
                                        View nestedChild = ((ViewGroup) child).getChildAt(j);
                                        if (nestedChild instanceof TextView) {
                                            TextView textView = (TextView) nestedChild;
                                            // Assume the first TextView is the name
                                            itemName = textView.getText().toString();
                                            break;
                                        }
                                    }
                                }
                            }
                            
                            // Generate a unique ID from the name
                            itemId = "home_" + itemName.toLowerCase().replace(" ", "_");
                            
                            // Get a reasonable price based on the item name
                            if (itemName.contains("Chole")) {
                                itemPrice = 150.0;
                                imageResource = R.drawable.image_1;
                            } else if (itemName.contains("Shake")) {
                                itemPrice = 60.0;
                                imageResource = R.drawable.image;
                            } else if (itemName.contains("Coffee")) {
                                itemPrice = 80.0;
                                imageResource = R.drawable.image_3;
                            } else {
                                itemPrice = 100.0;
                                imageResource = R.drawable.image_4;
                            }
                            
                            // Create a MenuItem
                            MenuItem menuItem = new MenuItem(itemId, itemName, itemPrice, imageResource);
                            
                            // Add to cart using CartManager
                            mCartManager.addToCart(menuItem, this);
                            
                        } catch (Exception e) {
                            Log.e(TAG, "Error getting item details", e);
                            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                findAndSetupAddToCartButtons(viewGroup.getChildAt(i));
            }
        }
    }
} 