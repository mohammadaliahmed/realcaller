package com.appsinventiv.realcaller.Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appsinventiv.realcaller.Adapters.SimpleFragmentPagerAdapter;
import com.appsinventiv.realcaller.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class CallLogsFragment extends Fragment {
    private View rootView;

    FloatingActionButton fab2, fab3, fab4;
    boolean flag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.call_logs_fragment, container, false);

        fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) rootView.findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) rootView.findViewById(R.id.fab4);

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    fab2.show();
                    fab3.show();
                    fab2.animate().translationY(-(fab3.getCustomSize() + fab4.getCustomSize()));
                    fab3.animate().translationY(-(fab4.getCustomSize()));

                    fab4.setImageResource(R.drawable.ic_dial);
                    flag = false;

                } else {
                    fab2.hide();
                    fab3.hide();
                    fab2.animate().translationY(0);
                    fab3.animate().translationY(0);

                    fab4.setImageResource(R.drawable.ic_dial);
                    flag = true;

                }
            }
        });

        return rootView;

    }


}
