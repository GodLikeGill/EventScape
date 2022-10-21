package com.group5.eventscape.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.group5.eventscape.fragments.ExploreFragment;
import com.group5.eventscape.fragments.FavoriteFragment;
import com.group5.eventscape.fragments.HomeFragment;
import com.group5.eventscape.fragments.ProfileFragment;

public class PageViewerAdapter extends FragmentPagerAdapter {

    public PageViewerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ExploreFragment();
            case 2:
                return new FavoriteFragment();
            case 3:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
