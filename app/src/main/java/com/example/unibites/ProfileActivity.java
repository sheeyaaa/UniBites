package com.example.unibites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set up back button
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> onBackPressed());
        }

        // Set up menu options click listeners
        setupMenuOptions();
    }

    private void setupMenuOptions() {
        // Personal Info
        View personalInfoOption = findViewById(R.id.personalInfoOption);
        if (personalInfoOption != null) {
            personalInfoOption.setOnClickListener(v -> {
                Toast.makeText(this, "Personal Info clicked", Toast.LENGTH_SHORT).show();
                // Launch personal info activity when implemented
            });
        }

        // Addresses
        View addressesOption = findViewById(R.id.addressesOption);
        if (addressesOption != null) {
            addressesOption.setOnClickListener(v -> {
                Toast.makeText(this, "Addresses clicked", Toast.LENGTH_SHORT).show();
                // Launch addresses activity when implemented
            });
        }

        // Cart
        View cartOption = findViewById(R.id.cartOption);
        if (cartOption != null) {
            cartOption.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }

        // Favorite
        View favoriteOption = findViewById(R.id.favoriteOption);
        if (favoriteOption != null) {
            favoriteOption.setOnClickListener(v -> {
                Toast.makeText(this, "Favorites coming soon", Toast.LENGTH_SHORT).show();
                // Launch UnderDevelopmentActivity
                Intent intent = new Intent(ProfileActivity.this, UnderDevelopmentActivity.class);
                intent.putExtra("activity_name", "Favorites");
                startActivity(intent);
            });
        }

        // Notifications
        View notificationsOption = findViewById(R.id.notificationsOption);
        if (notificationsOption != null) {
            notificationsOption.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, UnderDevelopmentActivity.class);
                intent.putExtra("activity_name", "Notifications");
                startActivity(intent);
            });
        }

        // Payment Method
        View paymentMethodOption = findViewById(R.id.paymentMethodOption);
        if (paymentMethodOption != null) {
            paymentMethodOption.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, UnderDevelopmentActivity.class);
                intent.putExtra("activity_name", "Payment Methods");
                startActivity(intent);
            });
        }

        // FAQs
        View faqsOption = findViewById(R.id.faqsOption);
        if (faqsOption != null) {
            faqsOption.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, UnderDevelopmentActivity.class);
                intent.putExtra("activity_name", "FAQs");
                startActivity(intent);
            });
        }

        // User Reviews
        View userReviewsOption = findViewById(R.id.userReviewsOption);
        if (userReviewsOption != null) {
            userReviewsOption.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, UnderDevelopmentActivity.class);
                intent.putExtra("activity_name", "User Reviews");
                startActivity(intent);
            });
        }

        // Settings
        View settingsOption = findViewById(R.id.settingsOption);
        if (settingsOption != null) {
            settingsOption.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, UnderDevelopmentActivity.class);
                intent.putExtra("activity_name", "Settings");
                startActivity(intent);
            });
        }

        // Log Out
        View logOutOption = findViewById(R.id.logOutOption);
        if (logOutOption != null) {
            logOutOption.setOnClickListener(v -> {
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
                // Return to SplashActivity or login screen
                Intent intent = new Intent(ProfileActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
} 