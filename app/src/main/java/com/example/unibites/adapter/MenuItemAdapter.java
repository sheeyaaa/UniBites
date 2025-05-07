package com.example.unibites.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unibites.R;
import com.example.unibites.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {
    private static final String TAG = "MenuItemAdapter";
    
    private final Context mContext;
    private final List<MenuItem> mMenuItems;
    private final OnMenuItemInteractionListener mListener;

    public interface OnMenuItemInteractionListener {
        void onQuantityIncreased(MenuItem item, int position);
        void onQuantityDecreased(MenuItem item, int position);
        void onAddToCart(MenuItem item, int position);
    }

    public MenuItemAdapter(Context context, List<MenuItem> menuItems, OnMenuItemInteractionListener listener) {
        this.mContext = context;
        this.mMenuItems = menuItems != null ? menuItems : new ArrayList<>();
        this.mListener = listener;
        Log.d(TAG, "MenuItemAdapter initialized with " + mMenuItems.size() + " items");
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem item = mMenuItems.get(position);
        Log.d(TAG, "Binding view for menu item: " + item.getName() + " at position " + position);
        
        // Set item details
        holder.itemNameTextView.setText(item.getName());
        holder.itemDescriptionTextView.setText(item.getDescription());
        holder.priceTextView.setText("₹" + String.format("%.2f", item.getPrice()));
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        
        // Show/hide add to cart button based on quantity
        holder.addToCartButton.setVisibility(item.getQuantity() > 0 ? View.VISIBLE : View.GONE);
        
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
                // Increase quantity
                item.setQuantity(item.getQuantity() + 1);
                holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
                
                // Show add to cart button if this is the first item added
                if (item.getQuantity() == 1) {
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                }
                
                mListener.onQuantityIncreased(item, adapterPosition);
            }
        });
        
        holder.decreaseButton.setOnClickListener(v -> {
            if (mListener != null && adapterPosition != RecyclerView.NO_POSITION && item.getQuantity() > 0) {
                // Decrease quantity
                item.setQuantity(item.getQuantity() - 1);
                holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
                
                // Hide add to cart button if quantity is now 0
                if (item.getQuantity() == 0) {
                    holder.addToCartButton.setVisibility(View.GONE);
                }
                
                mListener.onQuantityDecreased(item, adapterPosition);
            }
        });
        
        holder.addToCartButton.setOnClickListener(v -> {
            if (mListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                mListener.onAddToCart(item, adapterPosition);
                
                // Reset quantity after adding to cart
                item.setQuantity(0);
                holder.quantityTextView.setText("0");
                holder.addToCartButton.setVisibility(View.GONE);
                
                // Notify adapter of item change
                notifyItemChanged(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemNameTextView;
        TextView itemDescriptionTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageButton increaseButton;
        ImageButton decreaseButton;
        Button addToCartButton;

        MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
    
    public void updateItems(List<MenuItem> items) {
        mMenuItems.clear();
        if (items != null) {
            mMenuItems.addAll(items);
        }
        notifyDataSetChanged();
    }
} 