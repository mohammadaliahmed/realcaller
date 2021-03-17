package com.appsinventiv.realcaller.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsinventiv.realcaller.Activities.Fragments.ContactsFragment;
import com.appsinventiv.realcaller.Activities.Fragments.HomeFragment;
import com.appsinventiv.realcaller.Activities.Fragments.PremiumFragment;
import com.appsinventiv.realcaller.Models.NameAndPhone;
import com.appsinventiv.realcaller.Models.SaveContactModel;
import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.ApplicationClass;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.Constants;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.appsinventiv.realcaller.Utils.UserClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private double lng;
    private double lat;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment = new HomeFragment();
        loadFragment(fragment);


        WindowManager wm = (WindowManager) ApplicationClass.getInstance().getApplicationContext().getSystemService(WINDOW_SERVICE);
        if (checkOverlayDisplayPermission()) {
            // FloatingWindowGFG service is started
//            startService(new Intent(MainActivity.this, FloatingWindowGFG.class));
            // The MainActivity closes here
//            finish();
            uploadContactsToServer();
        } else {
            // If permission is not given,
            // it shows the AlertDialog box and
            // redirects to the Settings
            requestOverlayDisplayPermission();
        }

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.BIND_CALL_REDIRECTION_SERVICE};


        String[] PERMISSIONS2 = {
                Manifest.permission.ACCESS_FINE_LOCATION,
        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {

        }

        if (!hasPermissions(this, PERMISSIONS2)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            Intent intent = new Intent(MainActivity.this, GPSTrackerActivity.class);
            startActivityForResult(intent, 1);

        }


        Intent svc = new Intent(this, BroadcastService.class);

        startService(svc);

    }

    private void uploadContactsToServer() {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        List<NameAndPhone> list = new ArrayList<>();
        HashMap<String, String> contactsMap = SharedPrefs.getContactsMap();
//        if (contactsMap != null) {
//            for (Map.Entry<String, String> entry : contactsMap.entrySet()) {
//                String phone = entry.getKey();
//                String name = entry.getValue();
//                list.add(new NameAndPhone(name, phone));
//
//            }
//        }
//
//

        list.add(new NameAndPhone("Ahsan Jutt", "+923236994882"));
        list.add(new NameAndPhone("Dev", "+923236994883"));
        list.add(new NameAndPhone("Test tested", "+923236994884"));
        list.add(new NameAndPhone("Ali Ahmed", "+923158000333"));

        SaveContactModel model = new SaveContactModel(list);

        Call<ApiResponse> call = getResponse.saveContactList("berer " + SharedPrefs.getToken(), model);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {

//                    if (response.body().getStatus()) {
//                        SharedPrefs.setToken(response.body().getData().getAccessToken());
//                        Intent i = new Intent(Login.this, MainActivity.class);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//                        finish();
//                    } else {
//                        CommonUtils.showToast(response.body().getMessage());
//                    }
//                } else {
//                    try {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
////                         jObjError.getJSONObject("error").getString("message")
//                        CommonUtils.showToast(jObjError.getString("message").toString());
//
//                    } catch (Exception e) {
//                        CommonUtils.showToast(e.getMessage());
//                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
//                CommonUtils.showToast(t.getMessage());

            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                lng = extras.getDouble("Longitude");
                lat = extras.getDouble("Latitude");
                updateLocationToServer();
            }
        }
    }

    public void loadPremiumFragment() {
//        MenuItem item = navigation.getMenu().findItem(R.id.navigation_premium);
//
//        mOnNavigationItemSelectedListener.onNavigationItemSelected(item);
        navigation.setSelectedItemId(R.id.navigation_premium);


//        fragment = new PremiumFragment();
//        loadFragment(fragment);
    }

    private void updateLocationToServer() {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        JsonObject map = new JsonObject();
        map.addProperty("phone", SharedPrefs.getPhone());
        map.addProperty("lat", lat);
        map.addProperty("lon", lng);
        Call<ApiResponse> call = getResponse.updateLatLong(map, "berer " + SharedPrefs.getToken());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        Data data = response.body().getData();

                    } else {
                        CommonUtils.showToast(response.body().getMessage());
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                         jObjError.getJSONObject("error").getString("message")
                        CommonUtils.showToast(jObjError.getString("message").toString());

                    } catch (Exception e) {
                        CommonUtils.showToast(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
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
                    fragment = new ContactsFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_premium:
                    fragment = new PremiumFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_block:
//                    fragment = new FAQsFragment();
                    loadFragment(fragment);
                    return true;


            }
            return false;
        }
    };

    private boolean checkOverlayDisplayPermission() {
        // Android Version is lesser than Marshmallow
        // or the API is lesser than 23
        // doesn't need 'Display over other apps' permission enabling.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // If 'Display over other apps' is not enabled it
            // will return false or else true
            if (!Settings.canDrawOverlays(this)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void requestOverlayDisplayPermission() {
        // An AlertDialog is created
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // This dialog can be closed, just by
        // taping outside the dialog-box
        builder.setCancelable(true);

        // The title of the Dialog-box is set
        builder.setTitle("Screen Overlay Permission Needed");

        // The message of the Dialog-box is set
        builder.setMessage("Enable 'Display over other apps' from System Settings.");

        // The event of the Positive-Button is set
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The app will redirect to the 'Display over other apps' in Settings.
                // This is an Implicit Intent. This is needed when any Action is needed
                // to perform, here it is
                // redirecting to an other app(Settings).
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                // This method will start the intent. It takes two parameter,
                // one is the Intent and the other is
                // an requestCode Integer. Here it is -1.
                startActivityForResult(intent, RESULT_OK);
            }
        });
        AlertDialog dialog = builder.create();
        // The Dialog will show in the screen
        dialog.show();
    }

}
