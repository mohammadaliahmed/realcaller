package com.appsinventiv.realcaller.Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appsinventiv.realcaller.Adapters.SimpleFragmentPagerAdapter;
import com.appsinventiv.realcaller.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private View rootView;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.users,
            R.drawable.star,
            R.drawable.briefcase,
            R.drawable.shield
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);

        viewPager = rootView.findViewById(R.id.viewpager);


        // Give the TabLayout the ViewPager
        tabLayout = rootView.findViewById(R.id.sliding_tabs);


        List<String> departmentList = new ArrayList<>();
        departmentList.add("");
        departmentList.add("");
        departmentList.add("");
        departmentList.add("");
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getContext(), getChildFragmentManager(), departmentList);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

        return rootView;

    }


}
