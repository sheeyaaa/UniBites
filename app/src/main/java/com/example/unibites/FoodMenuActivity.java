package com.example.unibites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unibites.adapter.MenuItemsAdapter;
import com.example.unibites.model.MenuItem;
import com.example.unibites.utils.CartManager;
import com.example.unibites.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FoodMenuActivity extends BaseActivity implements MenuItemsAdapter.OnMenuItemClickListener {
    private static final String TAG = "FoodMenuActivity";

    private ImageButton mNotificationButton;
    private View mSearchLayout;
    
    private RecyclerView mLunchClassicsRecyclerView;
    private RecyclerView mHotSippersRecyclerView;
    private RecyclerView mEveningMunchiesRecyclerView;
    private RecyclerView mSweetTreatsRecyclerView;
    
    private MenuItemsAdapter mLunchAdapter;
    private MenuItemsAdapter mHotSippersAdapter;
    private MenuItemsAdapter mEveningMunchiesAdapter;
    private MenuItemsAdapter mSweetTreatsAdapter;
    
    private List<MenuItem> mLunchItems = new ArrayList<>();
    private List<MenuItem> mHotSippersItems = new ArrayList<>();
    private List<MenuItem> mEveningMunchiesItems = new ArrayList<>();
    private List<MenuItem> mSweetTreatsItems = new ArrayList<>();
    
    // Cart manager reference
    private CartManager mCartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        
        // Get cart manager instance
        mCartManager = CartManager.getInstance();
        
        // Initialize UI components
        initViews();
        
        // Setup data
        setupMenuData();
        
        // Setup RecyclerViews
        setupRecyclerViews();
        
        // Setup listeners
        setupListeners();
        
        // Setup footer navigation
        setupFooterNavigation();
    }
    
    private void initViews() {
        mNotificationButton = findViewById(R.id.notificationButton);
        mSearchLayout = findViewById(R.id.searchLayout);
        
        // Find all RecyclerViews
        mLunchClassicsRecyclerView = findViewById(R.id.lunchClassicsRecyclerView);
        mHotSippersRecyclerView = findViewById(R.id.hotSippersRecyclerView);
        mEveningMunchiesRecyclerView = findViewById(R.id.eveningMunchiesRecyclerView);
        mSweetTreatsRecyclerView = findViewById(R.id.sweetTreatsRecyclerView);
    }
    
    private void setupMenuData() {
        // Clear any existing items first
        mLunchItems.clear();
        mHotSippersItems.clear();
        mEveningMunchiesItems.clear();
        mSweetTreatsItems.clear();
        
        Log.d(TAG, "Setting up menu data...");
        
        // Lunch Time Classics
        mLunchItems.add(new MenuItem("1", "Chole Bhature", 150.0, R.drawable.image_1));
        mLunchItems.add(new MenuItem("2", "Rajma Chawal", 120.0, R.drawable.image));
        mLunchItems.add(new MenuItem("3", "Masala Dosa", 99.0, R.drawable.image_3));
        mLunchItems.add(new MenuItem("4", "Aloo Paratha", 85.0, R.drawable.image_4));
        mLunchItems.add(new MenuItem("5", "Paneer Butter Masala", 160.0, R.drawable.image));
        mLunchItems.add(new MenuItem("6", "Kadhai Paneer", 170.0, R.drawable.image_1));
        mLunchItems.add(new MenuItem("7", "Butter Naan", 25.0, R.drawable.image_3));
        mLunchItems.add(new MenuItem("8", "Veg Pulao", 110.0, R.drawable.image_4));
        
        Log.d(TAG, "Added " + mLunchItems.size() + " lunch items");
        
        // Hot Sippers
        mHotSippersItems.add(new MenuItem("9", "Tea", 20.0, R.drawable.image));
        mHotSippersItems.add(new MenuItem("10", "Coffee", 35.0, R.drawable.image_1));
        
        Log.d(TAG, "Added " + mHotSippersItems.size() + " hot sipper items");
        
        // Evening Munchies
        mEveningMunchiesItems.add(new MenuItem("11", "Honey Chili Potato", 120.0, R.drawable.image));
        mEveningMunchiesItems.add(new MenuItem("12", "French Fries", 99.0, R.drawable.image_1));
        mEveningMunchiesItems.add(new MenuItem("13", "Paneer Tikka", 150.0, R.drawable.image_3));
        
        Log.d(TAG, "Added " + mEveningMunchiesItems.size() + " evening munchies items");
        
        // Sweet Treats
        mSweetTreatsItems.add(new MenuItem("14", "Gulab Jamun", 45.0, R.drawable.image_4));
        mSweetTreatsItems.add(new MenuItem("15", "Chocolate Cake", 60.0, R.drawable.image));
        
        Log.d(TAG, "Added " + mSweetTreatsItems.size() + " sweet treats items");
    }
    
    private void setupRecyclerViews() {
        Log.d(TAG, "Setting up RecyclerViews...");
        
        // Define spacing in pixels (convert from dp)
        int spacingInPixels = (int) (16 * getResources().getDisplayMetrics().density);
        
        // Setup each RecyclerView
        
        // Lunch Classics
        Log.d(TAG, "Setting up Lunch Classics RecyclerView with " + mLunchItems.size() + " items");
        setupRecyclerView(mLunchClassicsRecyclerView, mLunchItems);
        mLunchClassicsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
        
        // Hot Sippers
        Log.d(TAG, "Setting up Hot Sippers RecyclerView with " + mHotSippersItems.size() + " items");
        setupRecyclerView(mHotSippersRecyclerView, mHotSippersItems);
        mHotSippersRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
        
        // Evening Munchies
        Log.d(TAG, "Setting up Evening Munchies RecyclerView with " + mEveningMunchiesItems.size() + " items");
        setupRecyclerView(mEveningMunchiesRecyclerView, mEveningMunchiesItems);
        mEveningMunchiesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
        
        // Sweet Treats
        Log.d(TAG, "Setting up Sweet Treats RecyclerView with " + mSweetTreatsItems.size() + " items");
        setupRecyclerView(mSweetTreatsRecyclerView, mSweetTreatsItems);
        mSweetTreatsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
        
        // Force a layout pass to ensure RecyclerViews update
        mLunchClassicsRecyclerView.requestLayout();
        mHotSippersRecyclerView.requestLayout();
        mEveningMunchiesRecyclerView.requestLayout();
        mSweetTreatsRecyclerView.requestLayout();
        
        Log.d(TAG, "RecyclerViews setup completed");
    }
    
    private void setupRecyclerView(RecyclerView recyclerView, List<MenuItem> items) {
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView is null");
            return;
        }
        
        if (items == null || items.isEmpty()) {
            Log.w(TAG, "Items list is null or empty");
            return;
        }
        
        // Remove any existing layouts and decorations
        if (recyclerView.getItemDecorationCount() > 0) {
            recyclerView.removeItemDecorationAt(0);
        }
        
        // Create a new grid layout manager with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        
        // Set up recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        
        // Create and set adapter
        MenuItemsAdapter adapter = new MenuItemsAdapter(this, items, this);
        recyclerView.setAdapter(adapter);
        
        // Store reference to the adapters
        if (recyclerView == mLunchClassicsRecyclerView) {
            mLunchAdapter = adapter;
        } else if (recyclerView == mHotSippersRecyclerView) {
            mHotSippersAdapter = adapter;
        } else if (recyclerView == mEveningMunchiesRecyclerView) {
            mEveningMunchiesAdapter = adapter;
        } else if (recyclerView == mSweetTreatsRecyclerView) {
            mSweetTreatsAdapter = adapter;
        }
        
        // Disable scrolling within the RecyclerView since it's inside a ScrollView
        recyclerView.setNestedScrollingEnabled(false);
        
        Log.d(TAG, "RecyclerView setup completed with " + items.size() + " items");
    }
    
    private void setupListeners() {
        // Set up notification button
        if (mNotificationButton != null) {
            mNotificationButton.setOnClickListener(v -> {
                Intent intent = new Intent(FoodMenuActivity.this, UnderDevelopmentActivity.class);
                intent.putExtra("activity_name", "Notifications");
                startActivity(intent);
            });
        }
        
        // Set up search
        if (mSearchLayout != null) {
            mSearchLayout.setOnClickListener(v -> {
                Toast.makeText(this, "Search functionality coming soon", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void onAddToCartClicked(MenuItem item, int position) {
        // Use CartManager to add item to cart
        mCartManager.addToCart(item, this);
    }
} 