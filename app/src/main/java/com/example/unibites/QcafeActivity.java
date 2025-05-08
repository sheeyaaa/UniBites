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

public class QcafeActivity extends AppCompatActivity {
    private static final String TAG = "QcafeActivity";
    private FirebaseFirestore db;
    private List<Product> qcafeProducts = new ArrayList<>();
    private RecyclerView recyclerViewQcafe;

    // Set the hotel type to filter for Qcafe
    private static final String TARGET_HOTEL_TYPE = "qcafe_shop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qcafe);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbarQcafe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Set up back button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        recyclerViewQcafe = findViewById(R.id.recyclerViewQcafe);
        recyclerViewQcafe.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewQcafe.setAdapter(new QcafeAdapter());

        // Fetch products for Qcafe
        fetchProductsByHotelType(TARGET_HOTEL_TYPE);
    }

    private void fetchProductsByHotelType(String hotelType) {
        db.collection("Products")
                .whereEqualTo("hotelType", hotelType)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        qcafeProducts.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                String id = document.getId();
                                String name = document.getString("Productname");
                                Double price = document.getDouble("ProductPrice");
                                String description = document.getString("description");
                                String imageUrl = document.getString("Productimage");
                                String category = document.getString("ProductCategory");

                                Product product = new Product(id, name, price, description, imageUrl, category);
                                qcafeProducts.add(product);
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing product", e);
                            }
                        }

                        if (recyclerViewQcafe.getAdapter() != null) {
                            recyclerViewQcafe.getAdapter().notifyDataSetChanged();
                        }

                        Log.d(TAG, "Found " + qcafeProducts.size() + " products for hotel type: " + hotelType);

                        if (qcafeProducts.isEmpty()) {
                            Toast.makeText(QcafeActivity.this, "No products found for this hotel type", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.e(TAG, "Error fetching products", task.getException());
                        Toast.makeText(QcafeActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class QcafeAdapter extends RecyclerView.Adapter<QcafeAdapter.ProductViewHolder> {

        private Random random = new Random();

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_item, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Product product = qcafeProducts.get(position);

            holder.productName.setText(product.getName());
            holder.productPrice.setText(String.format(Locale.US, "₹%.2f", product.getPrice()));

            float rating = 3.5f + random.nextFloat() * 1.5f;
            holder.tvRating.setText(String.format(Locale.US, "%.1f", rating));

            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(QcafeActivity.this)
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_placeholder_image)
                        .error(R.drawable.ic_placeholder_image)
                        .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(R.drawable.ic_placeholder_image);
            }

            holder.btnAddToCart.setOnClickListener(v -> {
                Toast.makeText(QcafeActivity.this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            });

            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(QcafeActivity.this, "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return qcafeProducts.size();
        }

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
