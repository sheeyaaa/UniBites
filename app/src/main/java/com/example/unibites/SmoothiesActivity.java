package com.example.unibites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SmoothiesActivity extends AppCompatActivity {
    private static final String TAG = "SmoothiesActivity";
    private FirebaseFirestore db;
    private List<Product> smoothiesProducts = new ArrayList<>();
    private RecyclerView recyclerViewSmoothies;

    // Set the hotel type you want to filter by
    private static final String TARGET_HOTEL_TYPE = "smoothie_shop"; // Change this to your desired hotel type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_smoothies);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbarSmoothies);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Set up back button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView with a Grid Layout (2 columns)
        recyclerViewSmoothies = findViewById(R.id.recyclerViewSmoothies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewSmoothies.setLayoutManager(gridLayoutManager);

        // Set up the RecyclerView with inline adapter
        recyclerViewSmoothies.setAdapter(new SmoothiesAdapter());

        // Fetch products from Firestore filtered by hotel type
        fetchProductsByHotelType(TARGET_HOTEL_TYPE);
    }

    private void fetchProductsByHotelType(String hotelType) {
        db.collection("Products")
                .whereEqualTo("hotelType", hotelType)  // Filter by hotel type
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        smoothiesProducts.clear(); // Clear existing list

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                String id = document.getId();
                                String name = document.getString("Productname");
                                Double price = document.getDouble("ProductPrice");
                                String description = document.getString("description");
                                String imageUrl = document.getString("Productimage");
                                String category = document.getString("ProductCategory");

                                // Create product object
                                Product product = new Product(id, name, price, description, imageUrl, category);
                                smoothiesProducts.add(product);

                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing product", e);
                            }
                        }

                        // Notify adapter about data change
                        if (recyclerViewSmoothies.getAdapter() != null) {
                            recyclerViewSmoothies.getAdapter().notifyDataSetChanged();
                        }

                        // Log the number of products found
                        Log.d(TAG, "Found " + smoothiesProducts.size() + " products for hotel type: " + hotelType);

                        if (smoothiesProducts.isEmpty()) {
                            Toast.makeText(SmoothiesActivity.this, "No products found for this hotel type", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.e(TAG, "Error fetching products", task.getException());
                        Toast.makeText(SmoothiesActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Inner adapter class - no need for a separate file
    private class SmoothiesAdapter extends RecyclerView.Adapter<SmoothiesAdapter.ProductViewHolder> {

        private Random random = new Random(); // For generating random ratings

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_item, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Product product = smoothiesProducts.get(position);

            // Set product name
            holder.productName.setText(product.getName());

            // Set product price with formatting
            holder.productPrice.setText(String.format(Locale.US, "₹%.2f", product.getPrice()));

            // Generate a random rating between 3.5 and 5.0
            float rating = 3.5f + random.nextFloat() * 1.5f;
            holder.tvRating.setText(String.format(Locale.US, "%.1f", rating));

            // Load product image
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(SmoothiesActivity.this)
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_placeholder_image)
                        .error(R.drawable.ic_placeholder_image)
                        .into(holder.productImage);
            } else {
                // Set a default image if no image URL is provided
                holder.productImage.setImageResource(R.drawable.ic_placeholder_image);
            }

            // Set click listener for the add to cart button
            holder.btnAddToCart.setOnClickListener(v -> {
                // TODO: Implement your add to cart functionality
                Toast.makeText(SmoothiesActivity.this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            });

            // Set click listener for the entire item
            holder.itemView.setOnClickListener(v -> {
                // TODO: Navigate to product details or perform other actions
                Toast.makeText(SmoothiesActivity.this, "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return smoothiesProducts.size();
        }

        // ViewHolder class
        class ProductViewHolder extends RecyclerView.ViewHolder {
            ImageView productImage;
            TextView productName;
            TextView productPrice;
            TextView tvRating;
            Button btnAddToCart;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.productImage);
                productName = itemView.findViewById(R.id.productName);
                productPrice = itemView.findViewById(R.id.productPrice);
                tvRating = itemView.findViewById(R.id.tv_rating);
                btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            }
        }
    }
}