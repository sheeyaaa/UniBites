package com.example.unibites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private LinearLayout emptyState;
    private ProgressBar progressBar;
    private ImageView backIcon;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        recyclerViewOrders = view.findViewById(R.id.recycler_view_orders);
        emptyState = view.findViewById(R.id.empty_state);
        progressBar = view.findViewById(R.id.progress_bar);
        backIcon = view.findViewById(R.id.back_icon);

        // Set up back button
        backIcon.setOnClickListener(v -> requireActivity().onBackPressed());

        // Set up RecyclerView
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(requireContext(), orderList);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewOrders.setAdapter(orderAdapter);

        // Load orders
        loadOrders();
    }

    private void loadOrders() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            showEmptyState();
            return;
        }

        String userId = currentUser.getUid();

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewOrders.setVisibility(View.GONE);
        emptyState.setVisibility(View.GONE);

        db.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        orderList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);
                            orderList.add(order);
                        }

                        orderAdapter.notifyDataSetChanged();

                        if (orderList.isEmpty()) {
                            showEmptyState();
                        } else {
                            hideEmptyState();
                        }
                    } else {
                        showEmptyState();
                    }
                });
    }

    private void showEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
        recyclerViewOrders.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        emptyState.setVisibility(View.GONE);
        recyclerViewOrders.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
