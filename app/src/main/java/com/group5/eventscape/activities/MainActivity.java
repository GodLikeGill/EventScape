package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.PageViewerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        FloatingActionButton fab = findViewById(R.id.fab_post);

        PageViewerAdapter adapter = new PageViewerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.explore:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.favorites:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.profile:
                    viewPager.setCurrentItem(3);
                    break;
            }
            return true;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.explore);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.favorites);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.profile);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, AddEventActivity.class));
        });
    }
}