package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.MyEventsAdapter;
import com.group5.eventscape.adapters.MyPurchasesAdapter;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Orders;
import com.group5.eventscape.viewmodels.EventViewModel;
import com.group5.eventscape.viewmodels.OrdersViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyPurchasesActivity extends AppCompatActivity {

    ImageButton back;
    MyPurchasesAdapter adapter;
    RecyclerView recyclerView;
    OrdersViewModel ordersViewModel;
    List<Orders> myOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);

        back = findViewById(R.id.ibBackMyPurchases);
        recyclerView = findViewById(R.id.rvMyPurchases);

        adapter = new MyPurchasesAdapter(myOrders);
        ordersViewModel = OrdersViewModel.getInstance(getApplication());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(v -> finish());

        this.getMyPurchases();

    }

    private void getMyPurchases() {
        ordersViewModel.getOrdersOfCurUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ordersViewModel.userOrders.observe(this, orders -> {
            myOrders.clear();
            for (Orders ord : orders) {
                myOrders.add(ord);
            }
            adapter.notifyDataSetChanged();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.getMyPurchases();
        Log.d("TAG", "onResume: " + myOrders);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}