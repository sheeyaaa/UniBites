package com.example.unibites;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderDate, tvOrderStatus, tvPaymentMethod;
    private TextView tvSubtotal, tvTax, tvTotal;
    private LinearLayout orderItemsContainer;
    private ProgressBar progressBar;
    private ImageView backIcon;

    private FirebaseFirestore db;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Get order ID from intent
        orderId = getIntent().getStringExtra("ORDER_ID");
        if (orderId == null) {
            Toast.makeText(this, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize views
        tvOrderId = findViewById(R.id.tv_order_id);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);
        orderItemsContainer = findViewById(R.id.order_items_container);
        progressBar = findViewById(R.id.progress_bar);
        backIcon = findViewById(R.id.back_icon);

        // Set up back button
        backIcon.setOnClickListener(v -> onBackPressed());

        // Load order details
        loadOrderDetails();
    }

    private void loadOrderDetails() {
        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);

        db.collection("orders")
                .document(orderId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Order order = document.toObject(Order.class);
                            displayOrderDetails(order);
//                            loadOrderItems();
                        } else {
                            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Error loading order: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void displayOrderDetails(Order order) {
        tvOrderId.setText("Order #" + order.getOrderId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a", Locale.getDefault());
        tvOrderDate.setText(dateFormat.format(order.getOrderDate()));

        tvOrderStatus.setText(order.getOrderStatus());
        switch (order.getOrderStatus()) {
            case "COMPLETED":
                tvOrderStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
                break;
            case "PENDING":
                tvOrderStatus.setTextColor(Color.parseColor("#FF9800")); // Orange
                break;
            case "CANCELLED":
                tvOrderStatus.setTextColor(Color.parseColor("#F44336")); // Red
                break;
        }

        tvPaymentMethod.setText(order.getPaymentMethod());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedSubtotal = currencyFormat.format(order.getSubtotal()).replace("₹", "₹ ");
        String formattedTax = currencyFormat.format(order.getTax()).replace("₹", "₹ ");
        String formattedTotal = currencyFormat.format(order.getTotal()).replace("₹", "₹ ");

        tvSubtotal.setText(formattedSubtotal);
        tvTax.setText(formattedTax);
        tvTotal.setText(formattedTotal);
    }

//    private void loadOrderItems() {
//        db.collection("orders")
//                .document(orderId)
//                .collection("items")
//                .get()
//                .addOnCompleteListener(task -> {
//                    progressBar.setVisibility(View.GONE);
//
//                    if (task.isSuccessful()) {
//                        orderItemsContainer.removeAllViews();
//
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String itemName = document.getString("name");
//                            long quantity = document.getLong("quantity");
//                            double price = document.getDouble("price");
//
//                            View itemView = LayoutInflater.from(this).inflate(R.layout.item_order, orderItemsContainer, false);
//
//                            TextView tvItemName = itemView.findViewById(R.id.tv_item_name);
//                            TextView tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
//                            TextView tvItemPrice = itemView.findViewById(R.id.tv_item_price);
//
//                            tvItemName.setText(itemName);
//                            tvItemQuantity.setText("Qty: " + quantity);
//
//                            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
//                            String formattedPrice = currencyFormat.format(price).replace("₹", "₹ ");
//                            tvItemPrice.setText(formattedPrice);
//
//                            orderItemsContainer.addView(itemView);
//                        }
//                    } else {
//                        Toast.makeText(this, "Failed to load items", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
