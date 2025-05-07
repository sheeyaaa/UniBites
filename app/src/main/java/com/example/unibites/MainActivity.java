package com.example.unibites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Layout file for order history

        // Set title to make it clear this is Order History
        TextView titleTextView = findViewById(R.id.textView);
        if (titleTextView != null) {
            titleTextView.setText("Order History");
        }
        
        // Set up action for reorder button
        Button reorderButton = findViewById(R.id.button2); // ID must match the one in your XML
        if (reorderButton != null) {
            reorderButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                startActivity(intent);
            });
        }
        
        // Setup footer navigation
        setupFooterNavigation();
    }
}
