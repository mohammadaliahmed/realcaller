package com.appsinventiv.realcaller.Activities.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appsinventiv.realcaller.Activities.BroadcastService;
import com.appsinventiv.realcaller.Activities.MainActivity;
import com.appsinventiv.realcaller.Activities.Settings;
import com.appsinventiv.realcaller.Activities.Splash;
import com.appsinventiv.realcaller.Activities.WhoViewedMyProfile;
import com.appsinventiv.realcaller.Adapters.SimpleFragmentPagerAdapter;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private View rootView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    boolean navigationDrawerVisible;
    LinearLayout whoViewMyProfile, notification, upgrade, invite;

    ImageView menu;
    TextView logout, navName;
    RelativeLayout navigationDrawer;
    TextView feedback, settings;


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

        upgrade = rootView.findViewById(R.id.upgrade);
        notification = rootView.findViewById(R.id.notification);
        whoViewMyProfile = rootView.findViewById(R.id.whoViewMyProfile);
        feedback = rootView.findViewById(R.id.feedback);
        invite = rootView.findViewById(R.id.invite);
        settings = rootView.findViewById(R.id.settings);

        viewPager = rootView.findViewById(R.id.viewpager);
        navigationDrawer = rootView.findViewById(R.id.navigationDrawer);
        whoViewMyProfile = rootView.findViewById(R.id.whoViewMyProfile);
        logout = rootView.findViewById(R.id.logout);
        navName = rootView.findViewById(R.id.navName);

        menu = rootView.findViewById(R.id.menu);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showALert();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationDrawerVisible) {
                    navigationDrawer.setVisibility(View.GONE);
//                    slideDown(navigationDrawer);

                    navigationDrawerVisible = false;

                }

                ((MainActivity) getActivity()).loadPremiumFragment();

            }
        });


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
                startActivity(i);
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Real Caller\n Download Now\n" + "http://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                startActivity(Intent.createChooser(shareIntent, "Share App via.."));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Settings.class));
            }
        });


        navName.setText(SharedPrefs.getName() + "\n" + SharedPrefs.getPhone());


        // Give the TabLayout the ViewPager
        tabLayout = rootView.findViewById(R.id.sliding_tabs);
        whoViewMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WhoViewedMyProfile.class));
            }
        });

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


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navigationDrawerVisible) {
                    navigationDrawer.setVisibility(View.GONE);
//                    slideDown(navigationDrawer);

                    navigationDrawerVisible = false;

                } else {
                    navigationDrawerVisible = true;
                    navigationDrawer.setVisibility(View.VISIBLE);
//                    slideUp(navigationDrawer);
                }
            }
        });

        navigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationDrawerVisible) {
                    navigationDrawer.setVisibility(View.GONE);
//                    slideDown(navigationDrawer);

                    navigationDrawerVisible = false;

                }
            }
        });


        return rootView;

    }


    private void showALert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage("Do you want to logout? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent myService = new Intent(getActivity(), BroadcastService.class);
                getActivity().stopService(myService);
                SharedPrefs.logout();
                Intent intent = new Intent(getActivity(), Splash.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();


            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void slideUp(View view) {
        navigationDrawerVisible = true;
        view.animate().translationX(CommonUtils.pxFromDp(getContext(), 250)).start(); // move away
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        navigationDrawerVisible = false;
        view.animate().translationX(CommonUtils.pxFromDp(getContext(), 0)).start(); // move away

    }

}
