package com.group5.eventscape.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.eventscape.R;
import com.group5.eventscape.activities.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            if (sh.getBoolean("isLoggedIn", false))
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            else
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            finish();
        }, 2000);
    }
}