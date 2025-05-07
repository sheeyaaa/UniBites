package com.example.unibites;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UnderDevelopmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_development);
        
        Button backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        
        // Set message based on the activity that triggered this
        TextView messageTextView = findViewById(R.id.messageTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        if (messageTextView != null) {
            String activityName = getIntent().getStringExtra("activity_name");
            if (activityName != null && !activityName.isEmpty()) {
                messageTextView.setText(activityName + " is under development");
            } else {
                // Default welcome message
                messageTextView.setText("Welcome to UniBites!");
                if (descriptionTextView != null) {
                    descriptionTextView.setText("Your one-stop solution for campus food ordering. Use the navigation below to explore the app.");
                }
            }
        }
        
        // Setup footer navigation
        setupFooterNavigation();
    }
} 