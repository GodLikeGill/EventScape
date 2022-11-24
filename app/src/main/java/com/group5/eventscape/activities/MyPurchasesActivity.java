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
import com.group5.eventscape.adapters.MyPurchasesAdapter;
import com.group5.eventscape.models.Order;
import com.group5.eventscape.viewmodels.OrderViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyPurchasesActivity extends AppCompatActivity {

    ImageButton back;
    MyPurchasesAdapter adapter;
    RecyclerView recyclerView;
    OrderViewModel ordersViewModel;
    List<Order> myOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);

        back = findViewById(R.id.ibBackMyPurchases);
        recyclerView = findViewById(R.id.rvMyPurchases);

        adapter = new MyPurchasesAdapter(myOrders);
        ordersViewModel = OrderViewModel.getInstance(getApplication());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(v -> finish());

        this.getMyPurchases();

    }

    private void getMyPurchases() {
        ordersViewModel.getOrdersOfCurUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ordersViewModel.userOrders.observe(this, orders -> {
            myOrders.clear();
            // myOrders.addAll(orders);

            //sorting my purchase start
            List<Order> sortedList = orders;
            Comparator<Order> eventDateSorter
                    = (o1, o2) -> o1.getOrderDateTimeStamp().compareTo(o2.getOrderDateTimeStamp());

            Collections.sort(sortedList, eventDateSorter);
            Collections.reverse(sortedList);
            myOrders.addAll(sortedList);
            //sorting my purchase end

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