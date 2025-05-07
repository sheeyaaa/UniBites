package com.example.unibites.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unibites.R;
import com.example.unibites.model.CartItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private static final String TAG = "CartAdapter";
    
    private final Context mContext;
    private final List<CartItem> mCartItems;
    private final Map<String, String> mCategoryMap;
    private final OnQuantityChangeListener mListener;

    public interface OnQuantityChangeListener {
        void onQuantityIncreased(CartItem item, int position);
        void onQuantityDecreased(CartItem item, int position);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnQuantityChangeListener listener) {
        this.mContext = context;
        this.mCartItems = cartItems != null ? cartItems : new ArrayList<>();
        this.mListener = listener;
        this.mCategoryMap = new HashMap<>();
        
        Log.d(TAG, "CartAdapter initialized with " + mCartItems.size() + " items");
        
        // Group items by category (for now just using "Food" for all)
        for (CartItem item : mCartItems) {
            // You can extend this to use real categories from Firebase later
            // This is currently a hardcoded categorization that would be replaced with Firebase data
            if (item.getName().toLowerCase().contains("coffee") || 
                    item.getName().toLowerCase().contains("shake")) {
                mCategoryMap.put(item.getItemId(), "Beverages");
            } else {
                mCategoryMap.put(item.getItemId(), "Food Items");
            }
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = mCartItems.get(position);
        Log.d(TAG, "Binding view for item: " + item.getName() + " at position " + position);
        
        // Set category visibility
        boolean showCategory = position == 0 || 
                !mCategoryMap.getOrDefault(mCartItems.get(position - 1).getItemId(), "")
                        .equals(mCategoryMap.getOrDefault(item.getItemId(), ""));
                
        if (showCategory) {
            holder.categoryTextView.setVisibility(View.VISIBLE);
            holder.categoryTextView.setText(mCategoryMap.getOrDefault(item.getItemId(), "Items"));
        } else {
            holder.categoryTextView.setVisibility(View.GONE);
        }
        
        // Set item details
        holder.itemNameTextView.setText(item.getName());
        holder.priceTextView.setText("₹" + String.format("%.2f", item.getPrice()));
        holder.itemDetailsTextView.setText("Cal (kcl): 25.00 | Qty: " + item.getQuantity());
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        
        // Set item image based on name - using available drawable resources
        if (item.getName().toLowerCase().contains("chole") || item.getName().toLowerCase().contains("curry")) {
            holder.itemImageView.setImageResource(R.drawable.image_1);
        } else if (item.getName().toLowerCase().contains("chocolate") || item.getName().toLowerCase().contains("burger")) {
            holder.itemImageView.setImageResource(R.drawable.image);
        } else if (item.getName().toLowerCase().contains("coffee") || item.getName().toLowerCase().contains("drink")) {
            holder.itemImageView.setImageResource(R.drawable.image_3);
        } else if (item.getName().toLowerCase().contains("pizza") || item.getName().toLowerCase().contains("pasta")) {
            holder.itemImageView.setImageResource(R.drawable.image_4);
        } else {
            // Fallback image for other items
            holder.itemImageView.setImageResource(R.drawable.ic_food);
        }
        
        // Set quantity controls
        final int adapterPosition = holder.getAdapterPosition();
        
        holder.increaseButton.setOnClickListener(v -> {
            if (mListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                mListener.onQuantityIncreased(item, adapterPosition);
            }
        });
        
        holder.decreaseButton.setOnClickListener(v -> {
            if (mListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                mListener.onQuantityDecreased(item, adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        ImageView itemImageView;
        TextView itemNameTextView;
        TextView priceTextView;
        TextView itemDetailsTextView;
        TextView quantityTextView;
        ImageButton increaseButton;
        ImageButton decreaseButton;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            itemDetailsTextView = itemView.findViewById(R.id.itemDetailsTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }
    
    public void updateItems(List<CartItem> items) {
        mCartItems.clear();
        if (items != null) {
            mCartItems.addAll(items);
        }
        notifyDataSetChanged();
    }
} 