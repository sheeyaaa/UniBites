package com.example.unibites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.unibites.model.CartItem;
import com.example.unibites.utils.CartManager;

import java.util.List;

public class PaymentActivity extends BaseActivity {
    private static final String TAG = "PaymentActivity";
    
    private CartManager mCartManager;
    private LinearLayout mItemsContainer;
    private TextView mTotalPayTextView;
    private TextView mItemTotalTextView;
    private TextView mGstTextView;
    private TextView mGrandTotalTextView;
    private Button mQrPayButton;
    private Button mUpiPayButton;
    private Button mCardPayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get CartManager instance
        mCartManager = CartManager.getInstance();
        
        // Initialize views
        mItemsContainer = findViewById(R.id.itemsContainer);
        mTotalPayTextView = findViewById(R.id.totalPayTextView);
        mItemTotalTextView = findViewById(R.id.itemTotalTextView);
        mGstTextView = findViewById(R.id.gstTextView);
        mGrandTotalTextView = findViewById(R.id.grandTotalTextView);
        mQrPayButton = findViewById(R.id.qrPayButton);
        mUpiPayButton = findViewById(R.id.upiPayButton);
        mCardPayButton = findViewById(R.id.cardPayButton);

        // Find the custom ImageView for the back icon
        ImageView backIcon = findViewById(R.id.back_icon);

        // Set a click listener to finish the activity when the back icon is clicked
        backIcon.setOnClickListener(v -> {
            finish(); // Go back to CartActivity
        });
        
        // Set up payment buttons
        setupPaymentButtons();
        
        // Display cart items
        displayCartItems();
        
        // Setup footer navigation
        setupFooterNavigation();
    }
    
    private void setupPaymentButtons() {
        // QR Code Payment (functional)
        if (mQrPayButton != null) {
            mQrPayButton.setOnClickListener(v -> {
                showQrCodeDialog();
            });
        }
        
        // UPI ID Payment (non-functional for now)
        if (mUpiPayButton != null) {
            mUpiPayButton.setOnClickListener(v -> {
                Toast.makeText(this, "UPI payment coming soon", Toast.LENGTH_SHORT).show();
            });
        }
        
        // Card Payment (non-functional for now)
        if (mCardPayButton != null) {
            mCardPayButton.setOnClickListener(v -> {
                Toast.makeText(this, "Card payment coming soon", Toast.LENGTH_SHORT).show();
            });
        }
    }
    
    private void showQrCodeDialog() {
        // Create and show QR code dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_qr_payment, null);
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        
        // Set up button in the dialog
        Button paymentCompleteButton = dialogView.findViewById(R.id.paymentCompleteButton);
        paymentCompleteButton.setOnClickListener(v -> {
            dialog.dismiss();
            processPayment();
        });
        
        dialog.show();
    }
    
    private void displayCartItems() {
        try {
            // Get cart items
            List<CartItem> cartItems = mCartManager.getCartItems();
            
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            
            // Clear previous items
            mItemsContainer.removeAllViews();
            
            // Calculate totals
            double itemTotal = mCartManager.getTotalCartValue();
            double gst = itemTotal * 0.09; // Assuming 9% GST
            double grandTotal = itemTotal + gst;
            
            // Add each cart item to the layout
            LayoutInflater inflater = LayoutInflater.from(this);
            
            for (CartItem item : cartItems) {
                View itemView = inflater.inflate(R.layout.layout_payment_item, mItemsContainer, false);
                
                // Find views in the item layout
                ImageView itemImageView = itemView.findViewById(R.id.itemImageView);
                TextView itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
                TextView quantityTextView = itemView.findViewById(R.id.quantityTextView);
                TextView priceTextView = itemView.findViewById(R.id.priceTextView);
                
                // Set item details
                itemNameTextView.setText(item.getName());
                quantityTextView.setText(String.valueOf(item.getQuantity()));
                priceTextView.setText(String.format("₹ %.2f", item.getTotalPrice()));
                
                // Set item image based on name - using available drawable resources
                if (item.getName().toLowerCase().contains("chole") || item.getName().toLowerCase().contains("curry")) {
                    itemImageView.setImageResource(R.drawable.image_1);
                } else if (item.getName().toLowerCase().contains("chocolate") || item.getName().toLowerCase().contains("burger")) {
                    itemImageView.setImageResource(R.drawable.image);
                } else if (item.getName().toLowerCase().contains("coffee") || item.getName().toLowerCase().contains("drink")) {
                    itemImageView.setImageResource(R.drawable.image_3);
                } else if (item.getName().toLowerCase().contains("pizza") || item.getName().toLowerCase().contains("pasta")) {
                    itemImageView.setImageResource(R.drawable.image_4);
                } else {
                    // Fallback image for other items
                    itemImageView.setImageResource(R.drawable.ic_food);
                }
                
                // Add item view to container
                mItemsContainer.addView(itemView);
            }
            
            // Update summary text views
            if (mTotalPayTextView != null) {
                mTotalPayTextView.setText(String.format("To Pay ₹ %.2f", grandTotal));
            }
            
            if (mItemTotalTextView != null) {
                mItemTotalTextView.setText(String.format("₹ %.2f", itemTotal));
            }
            
            if (mGstTextView != null) {
                mGstTextView.setText(String.format("₹ %.2f", gst));
            }
            
            if (mGrandTotalTextView != null) {
                mGrandTotalTextView.setText(String.format("₹ %.2f", grandTotal));
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error displaying cart items", e);
            Toast.makeText(this, "Error loading payment details", Toast.LENGTH_SHORT).show();
        }
    }

    private void processPayment() {
        // Simulate payment processing
        Toast.makeText(this, "Processing payment...", Toast.LENGTH_SHORT).show();
        
        // Add a slight delay to simulate processing
        mQrPayButton.setEnabled(false);
        mUpiPayButton.setEnabled(false);
        mCardPayButton.setEnabled(false);
        
        new android.os.Handler().postDelayed(() -> {
            // Clear the cart after successful payment
            mCartManager.clearCart();
            
            // Show success message
            Toast.makeText(this, "Payment successful! Order placed.", Toast.LENGTH_LONG).show();
            
            // Navigate to home screen
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear all activities on top
            startActivity(intent);
            finish();
        }, 1500); // 1.5 seconds delay
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
