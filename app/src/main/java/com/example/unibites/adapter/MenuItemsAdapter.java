package com.example.unibites.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unibites.R;
import com.example.unibites.model.MenuItem;

import java.util.List;

/**
 * Adapter for menu items in the food menu grid - creates views programmatically
 */
public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemViewHolder> {

    private final Context context;
    private final List<MenuItem> menuItems;
    private final OnMenuItemClickListener listener;
    private final int dpToPx;

    public interface OnMenuItemClickListener {
        void onAddToCartClicked(MenuItem item, int position);
    }

    public MenuItemsAdapter(Context context, List<MenuItem> menuItems, OnMenuItemClickListener listener) {
        this.context = context;
        this.menuItems = menuItems;
        this.listener = listener;
        
        // Calculate dp to pixels conversion factor
        float density = context.getResources().getDisplayMetrics().density;
        this.dpToPx = (int) (density + 0.5f);
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create CardView container
        CardView cardView = new CardView(context);
        cardView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        cardView.setCardElevation(2 * dpToPx);
        cardView.setRadius(10 * dpToPx);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(0, 0, 0, 0);
        
        // Create main LinearLayout
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        
        // Create ImageView for food image
        ImageView foodImageView = new ImageView(context);
        foodImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100 * dpToPx));
        foodImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        foodImageView.setId(View.generateViewId());
        
        // Create LinearLayout for details
        LinearLayout detailsLayout = new LinearLayout(context);
        detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100 * dpToPx));
        detailsLayout.setOrientation(LinearLayout.VERTICAL);
        detailsLayout.setPadding(8 * dpToPx, 6 * dpToPx, 8 * dpToPx, 6 * dpToPx);
        
        // Create TextView for food name
        TextView foodNameTextView = new TextView(context);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        foodNameTextView.setLayoutParams(nameParams);
        foodNameTextView.setMaxLines(1);
        foodNameTextView.setMinLines(1);
        foodNameTextView.setEllipsize(android.text.TextUtils.TruncateAt.END);
        foodNameTextView.setTextColor(context.getResources().getColor(R.color.text_dark));
        foodNameTextView.setTextSize(14);
        foodNameTextView.setId(View.generateViewId());
        
        // Create LinearLayout for price
        LinearLayout priceLayout = new LinearLayout(context);
        LinearLayout.LayoutParams priceLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        priceLayoutParams.topMargin = 4 * dpToPx;
        priceLayout.setLayoutParams(priceLayoutParams);
        priceLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        // Create TextView for currency symbol
        TextView currencyTextView = new TextView(context);
        currencyTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        currencyTextView.setText("₹");
        currencyTextView.setTextColor(context.getResources().getColor(R.color.orange_primary));
        currencyTextView.setTextSize(12);
        
        // Create TextView for price value
        TextView priceTextView = new TextView(context);
        LinearLayout.LayoutParams priceValueParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        priceValueParams.leftMargin = 2 * dpToPx;
        priceTextView.setLayoutParams(priceValueParams);
        priceTextView.setTextColor(context.getResources().getColor(R.color.orange_primary));
        priceTextView.setTextSize(12);
        priceTextView.setId(View.generateViewId());
        
        // Create spacer view
        View spacerView = new View(context);
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                0,
                0,
                1.0f);
        spacerView.setLayoutParams(spacerParams);
        
        // Create Add To Cart button
        Button addToCartButton = new Button(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                32 * dpToPx);
        addToCartButton.setLayoutParams(buttonParams);
        addToCartButton.setText("Add To Cart");
        addToCartButton.setTextSize(11);
        addToCartButton.setTextColor(context.getResources().getColor(android.R.color.white));
        addToCartButton.setAllCaps(false);
        addToCartButton.setBackgroundResource(R.drawable.rounded_button_bg);
        
        // Set orange color for the button
        addToCartButton.getBackground().setTint(context.getResources().getColor(R.color.orange_primary));
        
        addToCartButton.setId(View.generateViewId());
        
        // Add views to their parent layouts
        priceLayout.addView(currencyTextView);
        priceLayout.addView(priceTextView);
        
        detailsLayout.addView(foodNameTextView);
        detailsLayout.addView(priceLayout);
        detailsLayout.addView(spacerView);
        detailsLayout.addView(addToCartButton);
        
        mainLayout.addView(foodImageView);
        mainLayout.addView(detailsLayout);
        
        cardView.addView(mainLayout);
        
        // Create and return the ViewHolder with the generated view hierarchy
        return new MenuItemViewHolder(cardView, foodImageView, foodNameTextView, priceTextView, addToCartButton);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        
        // Set food image - first try using resource, then URL if available
        if (item.getImageResource() != 0) {
            holder.foodImageView.setImageResource(item.getImageResource());
        } else if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            // Here you'd use a library like Glide or Picasso to load from URL
            // For now, show a placeholder if no image resource
            holder.foodImageView.setImageResource(R.drawable.ic_placeholder_image);
        } else {
            // Set a default placeholder
            holder.foodImageView.setImageResource(R.drawable.ic_placeholder_image);
        }
        
        // Set name and price
        holder.foodNameTextView.setText(item.getName());
        holder.priceTextView.setText(String.format("%.2f", item.getPrice()));
        
        // Set add to cart button click listener
        holder.addToCartButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClicked(item, position);
            } else {
                Toast.makeText(context, item.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView foodNameTextView;
        TextView priceTextView;
        Button addToCartButton;

        public MenuItemViewHolder(View itemView, ImageView foodImageView, 
                                 TextView foodNameTextView, TextView priceTextView, 
                                 Button addToCartButton) {
            super(itemView);
            this.foodImageView = foodImageView;
            this.foodNameTextView = foodNameTextView;
            this.priceTextView = priceTextView;
            this.addToCartButton = addToCartButton;
        }
    }
} 