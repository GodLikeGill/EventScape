package com.group5.eventscape.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.MyPurchasesViewPagerAdapter;

public class MyPurchaseTabsActivity extends AppCompatActivity {

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchase_tabs);

        back = findViewById(R.id.ibBackMyPurchases);
        back.setOnClickListener(v -> finish());

        TabLayout tabLayout = findViewById(R.id.purchaseTabs);
        ViewPager2 viewPager2 = findViewById(R.id.purchaseViewPager);

        MyPurchasesViewPagerAdapter adapter = new MyPurchasesViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy(){
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position) {
                    case 0:
                        tab.setText("Upcoming Events");
                        break;
                    case 1:
                        tab.setText("Past Events");
                        break;
                }
            }
        }).attach();
    }
}