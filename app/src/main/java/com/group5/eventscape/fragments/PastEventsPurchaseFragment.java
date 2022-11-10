package com.group5.eventscape.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.MyPurchasesAdapter;
import com.group5.eventscape.databinding.FragmentPastEventsPurchaseBinding;
import com.group5.eventscape.models.Orders;
import com.group5.eventscape.viewmodels.OrdersViewModel;

import java.util.ArrayList;
import java.util.List;


public class PastEventsPurchaseFragment extends Fragment {
    private FragmentPastEventsPurchaseBinding binding;
    private String TAG = this.getClass().getCanonicalName();

    // define the data source for the adapter
    private List<Orders> myOrders = new ArrayList<>();
    private MyPurchasesAdapter adapter;
    private OrdersViewModel ordersViewModel;

    public PastEventsPurchaseFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPastEventsPurchaseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // create an instance of the adapter
        this.adapter = new MyPurchasesAdapter(this.myOrders);

        // configure the recyclerview to use the adapter
        binding.rvMyPurchases.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.rvMyPurchases.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        binding.rvMyPurchases.setAdapter(this.adapter);

        ordersViewModel = OrdersViewModel.getInstance(getActivity().getApplication());

        this.getMyPurchases();
    }

    private void getMyPurchases() {
        ordersViewModel.getOrdersOfCurUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ordersViewModel.userOrders.observe(getViewLifecycleOwner(), orders -> {
            myOrders.clear();
            for (Orders ord : orders) {
                myOrders.add(ord);
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
        this.getMyPurchases();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
        this.getMyPurchases();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop");
        this.getMyPurchases();
        this.adapter.notifyDataSetChanged();
    }
}