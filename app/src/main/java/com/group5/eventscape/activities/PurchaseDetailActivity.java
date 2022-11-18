package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.MyEventsAdapter;
import com.group5.eventscape.adapters.PurchaseDetailAdapter;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Order;
import com.group5.eventscape.viewmodels.EventViewModel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class PurchaseDetailActivity extends AppCompatActivity {


    ImageButton back;
    Order currentEvent;
    RecyclerView recyclerView;
    PurchaseDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);

        this.currentEvent = getIntent().getParcelableExtra("currentEvent");

        back = findViewById(R.id.ibBackPurchaseDetail);

        recyclerView = findViewById(R.id.rvPurchaseDetail);

        adapter = new PurchaseDetailAdapter(currentEvent);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(v -> finish());
    }
}