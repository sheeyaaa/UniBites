package com.example.unibites.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unibites.R;
import com.example.unibites.model.Order;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Order> mOrderList;
    private final OrderHistoryListener mListener;

    public interface OrderHistoryListener {
        void onReorderClicked(Order order);
    }

    public OrderHistoryAdapter(Context context, List<Order> orderList, OrderHistoryListener listener) {
        this.mContext = context;
        this.mOrderList = orderList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        
        // Set order number
        holder.orderNumberText.setText("Order #" + order.getOrderId());
        
        // Set order date
        holder.orderDateText.setText(order.getDate());
        
        // Set order items
        holder.orderItemsText.setText(order.getItems());
        
        // Set order amount
        holder.orderAmountText.setText("₹" + order.getAmount());
        
        // Set order status with appropriate styling
        holder.orderStatusText.setText(order.getStatus());
        
        // Set the image for the food item
        if (order.getImageResource() != null && !order.getImageResource().isEmpty()) {
            try {
                // Get the resource ID from the drawable name
                Resources resources = mContext.getResources();
                int resourceId = resources.getIdentifier(
                    order.getImageResource(), "drawable", mContext.getPackageName());
                
                if (resourceId != 0) {
                    holder.orderImageView.setImageResource(resourceId);
                }
            } catch (Exception e) {
                // If there's an error loading the image, keep the default
                e.printStackTrace();
            }
        }
        
        // Set status color based on status
        if (order.getStatus().equalsIgnoreCase("Delivered")) {
            holder.orderStatusText.setBackgroundTintList(mContext.getResources().getColorStateList(android.R.color.holo_green_light));
        } else if (order.getStatus().equalsIgnoreCase("Processing")) {
            holder.orderStatusText.setBackgroundTintList(mContext.getResources().getColorStateList(android.R.color.holo_blue_light));
        } else if (order.getStatus().equalsIgnoreCase("Cancelled")) {
            holder.orderStatusText.setBackgroundTintList(mContext.getResources().getColorStateList(android.R.color.holo_red_light));
        }
        
        // Set reorder button click listener
        holder.reorderButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onReorderClicked(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderNumberText;
        public TextView orderDateText;
        public TextView orderItemsText;
        public TextView orderAmountText;
        public TextView orderStatusText;
        public ImageView orderImageView;
        public Button reorderButton;

        public ViewHolder(View itemView) {
            super(itemView);
            orderNumberText = itemView.findViewById(R.id.orderNumberText);
            orderDateText = itemView.findViewById(R.id.orderDateText);
            orderItemsText = itemView.findViewById(R.id.orderItemsText);
            orderAmountText = itemView.findViewById(R.id.orderAmountText);
            orderStatusText = itemView.findViewById(R.id.orderStatusText);
            orderImageView = itemView.findViewById(R.id.orderImageView);
            reorderButton = itemView.findViewById(R.id.reorderButton);
        }
    }
} 