package com.SEG2505_Group8.mealer.UI.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Adapter extends FragmentStateAdapter {

    private final List<Fragment> fragments = new ArrayList<>();

    public ViewPager2Adapter(@NonNull FragmentManager fm, @NonNull Lifecycle lifecycle)
    {
        super(fm, lifecycle);
    }

    public void add(Fragment fragment)
    {
        fragments.add(fragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}