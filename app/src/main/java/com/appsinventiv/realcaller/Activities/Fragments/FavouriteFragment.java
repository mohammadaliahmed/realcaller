package com.appsinventiv.realcaller.Activities.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.realcaller.Activities.DialerActivity;
import com.appsinventiv.realcaller.Adapters.CallLogsAdapter;
import com.appsinventiv.realcaller.Models.CallLogsModel;
import com.appsinventiv.realcaller.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FavouriteFragment extends Fragment {
    private View rootView;

    FloatingActionButton fab2, fab3, fab4;
    boolean flag = true;
    TextView text;
    RecyclerView recycler;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
        recycler = rootView.findViewById(R.id.recycler);
        setupFloating();


        return rootView;

    }

    private void setupFloating() {
        fab2 = rootView.findViewById(R.id.fab2);
        fab3 = rootView.findViewById(R.id.fab3);
        fab4 = rootView.findViewById(R.id.fab4);
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
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DialerActivity.class));
            }
        });
    }


}
