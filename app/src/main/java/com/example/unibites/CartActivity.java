package com.example.unibites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.unibites.adapter.CartAdapter;
import com.example.unibites.model.CartItem;
import com.example.unibites.utils.CartManager;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity implements CartAdapter.OnQuantityChangeListener, CartManager.CartUpdateListener {
    private static final String TAG = "CartActivity";

    private RecyclerView mCartRecyclerView;
    private CartAdapter mCartAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyCartView;
    private Button mMakePaymentButton;
    private ImageButton mBackButton;
    private TextView mTotalPriceTextView;
    
    private CartManager mCartManager;
    private List<CartItem> mCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "Starting CartActivity onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cart);
            
            // Get CartManager instance
            mCartManager = CartManager.getInstance();
            
            // Initialize the cart items list
            try {
                mCartItems = new ArrayList<>();
                Log.d(TAG, "Cart items list initialized");
            } catch (Exception e) {
                Log.e(TAG, "Error initializing cart items list", e);
                Toast.makeText(this, "Error initializing cart", Toast.LENGTH_SHORT).show();
            }
            
            // Initialize views
            try {
                initViews();
                Log.d(TAG, "Views initialized");
            } catch (Exception e) {
                Log.e(TAG, "Error initializing views", e);
                Toast.makeText(this, "Error setting up UI", Toast.LENGTH_SHORT).show();
            }
            
            // Set up RecyclerView
            try {
                setupRecyclerView();
                Log.d(TAG, "RecyclerView set up successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error setting up RecyclerView", e);
                Toast.makeText(this, "Error setting up cart items", Toast.LENGTH_SHORT).show();
            }
            
            // Load cart items
            try {
                loadCartItems();
                Log.d(TAG, "Cart items loading initiated");
            } catch (Exception e) {
                Log.e(TAG, "Error loading cart items", e);
                Toast.makeText(this, "Error loading items", Toast.LENGTH_SHORT).show();
            }
            
            Log.d(TAG, "CartActivity onCreate completed");
            
            // Setup footer navigation
            setupFooterNavigation();
        } catch (Exception e) {
            Log.e(TAG, "Fatal error in CartActivity onCreate", e);
            Toast.makeText(this, "Error initializing cart: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish(); // Return to previous activity safely
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Register as listener to receive cart updates
        if (mCartManager != null) {
            mCartManager.registerListener(this);
        }
        // Refresh the cart items when returning to this activity
        loadCartItems();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister listener when activity is paused
        if (mCartManager != null) {
            mCartManager.unregisterListener(this);
        }
    }
    
    private void initViews() {
        try {
            mCartRecyclerView = findViewById(R.id.cartRecyclerView);
            if (mCartRecyclerView == null) {
                Log.e(TAG, "Cart RecyclerView not found in layout!");
            }
            
            mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
            mEmptyCartView = findViewById(R.id.emptyCartView);
            mMakePaymentButton = findViewById(R.id.makePaymentButton);
            mBackButton = findViewById(R.id.backButton);
            mTotalPriceTextView = findViewById(R.id.totalPriceTextView);
            
            // Set up swipe refresh
            mSwipeRefreshLayout.setOnRefreshListener(this::loadCartItems);
            
            // Set up back button
            mBackButton.setOnClickListener(v -> onBackPressed());
            
            // Set up make payment button
            mMakePaymentButton.setOnClickListener(v -> {
                if (mCartItems.isEmpty()) {
                    Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Navigate to payment activity
                    Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in initViews", e);
            throw e; // Re-throw to be caught by the main try-catch
        }
    }
    
    private void setupRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mCartRecyclerView.setLayoutManager(layoutManager);
            mCartAdapter = new CartAdapter(this, mCartItems, this);
            mCartRecyclerView.setAdapter(mCartAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error in setupRecyclerView", e);
            throw e; // Re-throw to be caught by the main try-catch
        }
    }
    
    private void loadCartItems() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            
            // Get cart items from CartManager
            mCartItems.clear();
            mCartItems.addAll(mCartManager.getCartItems());
            
            Log.d(TAG, "Loaded " + mCartItems.size() + " items from CartManager");
            
            if (mCartAdapter != null) {
                mCartAdapter.notifyDataSetChanged();
                Log.d(TAG, "Notified adapter of data change");
            } else {
                Log.e(TAG, "Cart adapter is null when trying to update");
            }
            
            // Update view based on cart contents
            updateCartUI();
            
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e(TAG, "Error in loadCartItems", e);
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            Toast.makeText(this, "Error loading cart items: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void updateCartUI() {
        if (mCartItems.isEmpty()) {
            Log.d(TAG, "Cart is empty, showing empty view");
            if (mEmptyCartView != null) {
                mEmptyCartView.setVisibility(View.VISIBLE);
            }
            if (mMakePaymentButton != null) {
                mMakePaymentButton.setEnabled(false);
            }
        } else {
            Log.d(TAG, "Cart has items, hiding empty view");
            if (mEmptyCartView != null) {
                mEmptyCartView.setVisibility(View.GONE);
            }
            if (mMakePaymentButton != null) {
                mMakePaymentButton.setEnabled(true);
            }
        }
        
        // Update total price
        if (mTotalPriceTextView != null) {
            double totalPrice = mCartManager.getTotalCartValue();
            mTotalPriceTextView.setText(String.format("₹%.2f", totalPrice));
        }
    }

    @Override
    public void onQuantityIncreased(CartItem item, int position) {
        try {
            // Use CartManager to increase item quantity
            mCartManager.increaseQuantity(item.getItemId());
        } catch (Exception e) {
            Log.e(TAG, "Error increasing quantity", e);
            Toast.makeText(this, "Error updating quantity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onQuantityDecreased(CartItem item, int position) {
        try {
            // Use CartManager to decrease item quantity
            mCartManager.decreaseQuantity(item.getItemId());
        } catch (Exception e) {
            Log.e(TAG, "Error decreasing quantity", e);
            Toast.makeText(this, "Error updating quantity", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onCartUpdated(List<CartItem> cartItems) {
        // Update UI when cart is changed
        mCartItems.clear();
        mCartItems.addAll(cartItems);
        
        if (mCartAdapter != null) {
            mCartAdapter.notifyDataSetChanged();
        }
        
        updateCartUI();
        
        // Update badge count
        updateCartBadge();
    }
} 