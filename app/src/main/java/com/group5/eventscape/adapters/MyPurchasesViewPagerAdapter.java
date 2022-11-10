package com.group5.eventscape.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.group5.eventscape.fragments.PastEventsPurchaseFragment;
import com.group5.eventscape.fragments.UpcomingEventsPurchaseFragment;

public class MyPurchasesViewPagerAdapter extends FragmentStateAdapter {
    public MyPurchasesViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new UpcomingEventsPurchaseFragment();
            default:
                return new PastEventsPurchaseFragment();
//            default:
//                return new UpcomingEventsPurchaseFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
