package com.example.unibites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.unibites.adapter.OrderHistoryAdapter;
import com.example.unibites.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends BaseActivity implements OrderHistoryAdapter.OrderHistoryListener {
    private static final String TAG = "OrderHistoryActivity";

    private RecyclerView mOrderHistoryRecyclerView;
    private OrderHistoryAdapter mOrderHistoryAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyHistoryView;
    private ImageButton mBackButton;
    private ImageButton mFilterButton;
    
    private List<Order> mOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "Starting OrderHistoryActivity onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order_history);
            
            // Initialize the order list
            mOrderList = new ArrayList<>();
            
            // Initialize views
            initViews();
            
            // Set up RecyclerView
            setupRecyclerView();
            
            // Load order history items
            loadOrderHistory();
            
            // Setup footer navigation
            setupFooterNavigation();
            
            Log.d(TAG, "OrderHistoryActivity onCreate completed");
        } catch (Exception e) {
            Log.e(TAG, "Fatal error in OrderHistoryActivity onCreate", e);
            Toast.makeText(this, "Error initializing order history: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish(); // Return to previous activity safely
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the orders when returning to this activity
        if (mOrderHistoryAdapter != null) {
            loadOrderHistory();
        }
    }
    
    private void initViews() {
        try {
            mOrderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);
            mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
            mEmptyHistoryView = findViewById(R.id.emptyHistoryView);
            mBackButton = findViewById(R.id.backButton);
            mFilterButton = findViewById(R.id.filterButton);
            
            // Set up swipe refresh
            mSwipeRefreshLayout.setOnRefreshListener(this::loadOrderHistory);
            
            // Set up back button
            mBackButton.setOnClickListener(v -> onBackPressed());
            
            // Set up filter button
            mFilterButton.setOnClickListener(v -> {
                Toast.makeText(this, "Filter functionality coming soon", Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in initViews", e);
            throw e;
        }
    }
    
    private void setupRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mOrderHistoryRecyclerView.setLayoutManager(layoutManager);
            mOrderHistoryAdapter = new OrderHistoryAdapter(this, mOrderList, this);
            mOrderHistoryRecyclerView.setAdapter(mOrderHistoryAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error in setupRecyclerView", e);
            throw e;
        }
    }
    
    private void loadOrderHistory() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            
            // In a real app, this would load from a database or API
            // For now, we'll add sample data
            mOrderList.clear();
            
            // Sample orders with appropriate image resources
            // The image resource name matches the item name
            mOrderList.add(new Order("123456", "15 Aug 2023", 
                "2x Chole Bhature, 1x Tea", 350.0, "Delivered", "image_1"));
                
            mOrderList.add(new Order("123455", "10 Aug 2023", 
                "1x Cold Coffee, 2x Samosa", 180.0, "Delivered", "image"));
                
            mOrderList.add(new Order("123454", "5 Aug 2023", 
                "1x Masala Dosa", 120.0, "Delivered", "image_3"));
                
            mOrderList.add(new Order("123453", "1 Aug 2023", 
                "2x Aloo Paratha, 1x Tea", 240.0, "Cancelled", "image_4"));
            
            mOrderHistoryAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
            
            // Show empty view if no orders
            if (mOrderList.isEmpty()) {
                mEmptyHistoryView.setVisibility(View.VISIBLE);
            } else {
                mEmptyHistoryView.setVisibility(View.GONE);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error in loadOrderHistory", e);
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            Toast.makeText(this, "Error loading order history: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onReorderClicked(Order order) {
        Toast.makeText(this, "Preparing to reorder " + order.getItems(), Toast.LENGTH_SHORT).show();
        
        // Navigate to ReorderActivity with order details
        Intent intent = new Intent(OrderHistoryActivity.this, ReorderActivity.class);
        intent.putExtra("order_id", order.getOrderId());
        intent.putExtra("order_items", order.getItems());
        intent.putExtra("order_amount", order.getAmount());
        intent.putExtra("order_image", order.getImageResource());
        startActivity(intent);
    }
} 