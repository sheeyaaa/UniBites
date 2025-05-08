package com.example.unibites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CheckoutActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView totalItemsPrice, totalGst, grandTotal;
    private Button payWithCashButton, payWithQrButton, payWithUpiButton;
    private ImageView backIcon;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<CartItem> cartItemList;

    private double subtotal = 0.0;
    private double tax = 0.0;
    private double total = 0.0;
    private final double TAX_RATE = 0.1; // 10% tax rate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        cartItemsContainer = findViewById(R.id.cart_items_container);
        totalItemsPrice = findViewById(R.id.total_items_price);
        totalGst = findViewById(R.id.total_gst);
        grandTotal = findViewById(R.id.grand_total);
        payWithCashButton = findViewById(R.id.pay_with_cash_button);
        payWithQrButton = findViewById(R.id.pay_with_qr_button);
        payWithUpiButton = findViewById(R.id.pay_with_upi_button);
        backIcon = findViewById(R.id.back_icon);

        // Initialize cart items list
        cartItemList = new ArrayList<>();

        // Set up click listeners
        backIcon.setOnClickListener(v -> onBackPressed());

        payWithCashButton.setOnClickListener(v -> processOrder("CASH"));
        payWithQrButton.setOnClickListener(v -> processOrder("QR"));
        payWithUpiButton.setOnClickListener(v -> processOrder("UPI"));

        // Load cart items
        loadCartItems();
    }

    private void loadCartItems() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();

        db.collection("carts")
                .document(userId)
                .collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cartItemList.clear();
                        subtotal = 0.0;
                        cartItemsContainer.removeAllViews();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CartItem cartItem = document.toObject(CartItem.class);
                            cartItem.setId(document.getId());
                            cartItemList.add(cartItem);
                            subtotal += cartItem.getTotalPrice();

                            // Add cart item view to container
                            addCartItemView(cartItem);
                        }

                        calculateTotals();

                        if (cartItemList.isEmpty()) {
                            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Error loading cart items: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void addCartItemView(CartItem cartItem) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.checkout_item, cartItemsContainer, false);

        TextView itemName = itemView.findViewById(R.id.item_name);
        TextView itemQuantity = itemView.findViewById(R.id.item_quantity);
        TextView itemPrice = itemView.findViewById(R.id.item_price);

        itemName.setText(cartItem.getName());
        itemQuantity.setText("x" + cartItem.getQuantity());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedPrice = currencyFormat.format(cartItem.getTotalPrice()).replace("$", "₹ ");
        itemPrice.setText(formattedPrice);

        cartItemsContainer.addView(itemView);
    }

    private void calculateTotals() {
        subtotal = 0.0;

        for (CartItem item : cartItemList) {
            subtotal += item.getTotalPrice();
        }

        tax = subtotal * TAX_RATE;
        total = subtotal + tax;

        // Format currency values for Indian Rupee
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedSubtotal = currencyFormat.format(subtotal).replace("$", "₹ ");
        String formattedTax = currencyFormat.format(tax).replace("$", "₹ ");
        String formattedTotal = currencyFormat.format(total).replace("$", "₹ ");

        totalItemsPrice.setText(formattedSubtotal);
        totalGst.setText(formattedTax);
        grandTotal.setText(formattedTotal);
    }

    private void processOrder(String paymentMethod) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String userId = currentUser.getUid();
        String orderId = UUID.randomUUID().toString();

        // Create order object
        Map<String, Object> order = new HashMap<>();
        order.put("userId", userId);
        order.put("orderId", orderId);
        order.put("orderDate", new Date());
        order.put("orderStatus", "PENDING");
        order.put("paymentMethod", paymentMethod);
        order.put("subtotal", subtotal);
        order.put("tax", tax);
        order.put("total", total);

        // Add order to Firestore
        db.collection("orders")
                .document(orderId)
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    // Add order items to Firestore
                    addOrderItems(orderId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to create order: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void addOrderItems(String orderId) {
        // Add each cart item to order_items collection
        for (CartItem item : cartItemList) {
            Map<String, Object> orderItem = new HashMap<>();
            orderItem.put("orderId", orderId);
            orderItem.put("itemId", item.getId());
            orderItem.put("itemName", item.getName());
            orderItem.put("price", item.getPrice());
            orderItem.put("quantity", item.getQuantity());
            orderItem.put("totalPrice", item.getTotalPrice());

            db.collection("orders")
                    .document(orderId)
                    .collection("items")
                    .add(orderItem);
        }

        // Clear user's cart after successful order
        clearUserCart();

        // Show success message
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();

        // Navigate back or to order confirmation
        finish();
    }

    private void clearUserCart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String userId = currentUser.getUid();

        // Delete all items in user's cart
        for (CartItem item : cartItemList) {
            db.collection("carts")
                    .document(userId)
                    .collection("items")
                    .document(item.getId())
                    .delete();
        }
    }
}