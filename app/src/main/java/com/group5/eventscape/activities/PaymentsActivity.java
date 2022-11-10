package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.group5.eventscape.R;

public class PaymentsActivity extends AppCompatActivity {

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        back = findViewById(R.id.ibBackPayments);

        back.setOnClickListener(v -> finish());
    }
}