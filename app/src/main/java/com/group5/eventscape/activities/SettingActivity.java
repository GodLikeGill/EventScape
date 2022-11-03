package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.group5.eventscape.R;

public class SettingActivity extends AppCompatActivity {

    ImageButton back;
    CardView cvPriSec;
    CardView cvPayments;
    CardView cvHelpSup;
    CardView cvAboutUs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        back = findViewById(R.id.ibBackMyEvents);
        cvPriSec = findViewById(R.id.cvPriSec);
        cvPayments = findViewById(R.id.cvPayments);
        cvHelpSup = findViewById(R.id.cvHelpSup);
        cvAboutUs = findViewById(R.id.cvAboutUs);

        cvPriSec.setOnClickListener(v -> Toast.makeText(SettingActivity.this, "Privacy & Security Clicked", Toast.LENGTH_LONG).show());
        cvPayments.setOnClickListener(v -> Toast.makeText(SettingActivity.this, "Payments Clicked", Toast.LENGTH_LONG).show());
        cvHelpSup.setOnClickListener(v -> Toast.makeText(SettingActivity.this, "Help & Support Clicked", Toast.LENGTH_LONG).show());
        cvAboutUs.setOnClickListener(v -> Toast.makeText(SettingActivity.this, "About Us Clicked", Toast.LENGTH_LONG).show());

        back.setOnClickListener(v -> finish());
    }
}