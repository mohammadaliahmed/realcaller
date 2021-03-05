package com.appsinventiv.realcaller.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsinventiv.realcaller.Activities.Fragments.HomeFragment;
import com.appsinventiv.realcaller.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragment = new HomeFragment();
        loadFragment(fragment);


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.BIND_CALL_REDIRECTION_SERVICE};
        //String val="";

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//            startForegroundService(new Intent(this, BroadcastService.class));
//        } else {
//            startService(new Intent(this, BroadcastService.class));
//        }

        Intent svc = new Intent(this, BroadcastService.class);

        startService(svc);

    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
//                    CommonUtils.showToast("granted");
                }
            }
        }
        return true;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView mBottomNavigationView = findViewById(R.id.navigation);
        if (mBottomNavigationView.getSelectedItemId() == R.id.navigation_home) {
            super.onBackPressed();
            finish();
        } else {
            mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:


                    fragment = new HomeFragment();
                    loadFragment(fragment);


                    return true;
                case R.id.navigation_contact:
//                    fragment = new NoticeBoardFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_premium:

//                    startActivity(new Intent(MainActivity.this, CreateTicket.class));
                    return true;
                case R.id.navigation_block:
//                    fragment = new FAQsFragment();
                    loadFragment(fragment);
                    return true;


            }
            return false;
        }
    };

}
