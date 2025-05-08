package com.example.unibites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private FirebaseFirestore db;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Format order ID
        String orderId = order.getOrderId();
        if (orderId.length() > 8) {
            // Truncate long IDs for better display
            orderId = orderId.substring(0, 8) + "...";
        }
        holder.tvOrderId.setText(orderId);

        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        holder.tvOrderDate.setText(dateFormat.format(order.getOrderDate()));

        // Format total
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String formattedTotal = currencyFormat.format(order.getTotal()).replace("$", "₹ ");
        holder.tvOrderTotal.setText(formattedTotal);

        // Set status and color
        holder.tvOrderStatus.setText(order.getOrderStatus());
        if (order.getOrderStatus().equals("COMPLETED")) {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.tvOrderStatus.setBackgroundResource(R.drawable.status_background);
        } else if (order.getOrderStatus().equals("PENDING")) {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#FF9800"));
            holder.tvOrderStatus.setBackgroundColor(Color.parseColor("#FFF3E0"));
        } else if (order.getOrderStatus().equals("CANCELLED")) {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#F44336"));
            holder.tvOrderStatus.setBackgroundColor(Color.parseColor("#FFEBEE"));
        }

        // Clear existing items
        holder.orderItemsContainer.removeAllViews();

        // Load order items from Firestore
        loadOrderItems(order.getOrderId(), holder.orderItemsContainer);

        // Set view details button click
        holder.btnViewDetails.setOnClickListener(v -> {
//            Intent intent = new Intent(context, OrderDetailActivity.class);
//            intent.putExtra("ORDER_ID", order.getOrderId());
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void loadOrderItems(String orderId, LinearLayout container) {
        db.collection("orders")
                .document(orderId)
                .collection("items")
                .limit(3) // Limit to 3 items for preview
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        container.removeAllViews();

                        int itemCount = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            itemCount++;

                            // Create item view
                            View itemView = LayoutInflater.from(context).inflate(
                                    R.layout.activity_order_detail, container, false);

                            // Get item data
                            String itemName = document.getString("itemName");
                            Long quantity = document.getLong("quantity");

//                            // Set item data
//                            TextView tvItemName = itemView.findViewById(R.id.tv_item_name);
////                          TextView tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
//
//                            tvItemName.setText(itemName);
//                            tvItemQuantity.setText("x" + quantity);

                            // Add to container
                            container.addView(itemView);
                        }

                        // Add "and X more items" if there are more than 3 items
                        if (task.getResult().size() > 3) {
                            TextView moreItemsView = new TextView(context);
                            moreItemsView.setText("and " + (task.getResult().size() - 3) + " more items...");
                            moreItemsView.setTextColor(Color.parseColor("#888888"));
                            moreItemsView.setPadding(0, 8, 0, 0);
                            container.addView(moreItemsView);
                        }

                        // If no items found
                        if (itemCount == 0) {
                            TextView noItemsView = new TextView(context);
                            noItemsView.setText("No items found");
                            noItemsView.setTextColor(Color.parseColor("#888888"));
                            container.addView(noItemsView);
                        }
                    }
                });
    }

    // ViewHolder class
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderStatus, tvOrderTotal;
        LinearLayout orderItemsContainer;
        Button btnViewDetails;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
            orderItemsContainer = itemView.findViewById(R.id.order_items_container);
            btnViewDetails = itemView.findViewById(R.id.btn_view_details);
        }
    }
}