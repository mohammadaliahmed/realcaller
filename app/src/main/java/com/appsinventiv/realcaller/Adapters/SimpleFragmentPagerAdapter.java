package com.appsinventiv.realcaller.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.appsinventiv.realcaller.Activities.Fragments.BlockedFragment;
import com.appsinventiv.realcaller.Activities.Fragments.CallLogsFragment;
import com.appsinventiv.realcaller.Activities.Fragments.FavouriteFragment;
import com.appsinventiv.realcaller.Activities.Fragments.SmsFragment;

import java.util.List;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    List<String> departmentList;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm, List<String> departmentList) {
        super(fm);
        mContext = context;
        this.departmentList = departmentList;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CallLogsFragment();
        } else if (position == 1) {
            return new FavouriteFragment();
        } else if (position == 2) {
            return new SmsFragment();
        } else if (position == 3) {
            return new BlockedFragment();
        } else {
            return null;
        }

    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return departmentList.size();
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return departmentList.get(position);
    }
}
