package com.appsinventiv.realcaller.Activities.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.viewpager.widget.ViewPager;

import com.appsinventiv.realcaller.Activities.DialerActivity;
import com.appsinventiv.realcaller.Activities.SearchNumber;
import com.appsinventiv.realcaller.Adapters.CallLogsAdapter;
import com.appsinventiv.realcaller.Adapters.SimpleFragmentPagerAdapter;
import com.appsinventiv.realcaller.Models.CallLogsModel;
import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.appsinventiv.realcaller.Utils.UserClient;
import com.google.android.gms.common.util.Strings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CallLogsFragment extends Fragment {
    private View rootView;

    FloatingActionButton fab2, fab3, fab4;
    boolean flag = true;
    TextView text;
    private List<CallLogsModel> callLogsList = new ArrayList<>();
    RecyclerView recycler;
    CallLogsAdapter adapter;
    private AlertDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.call_logs_fragment, container, false);
        recycler = rootView.findViewById(R.id.recycler);
        setupFloating();
        getPermissions();


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
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchNumber.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void readCallLogs() {

        Uri uriCallLogs = Uri.parse("content://call_log/calls");
        Cursor cursorCallLogs = getActivity().getContentResolver().query(uriCallLogs, null, null, null);
        cursorCallLogs.moveToFirst();
        do {
            String stringNumber = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.NUMBER));
            String stringDate = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.DATE));
            String stringName = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String stringDuration = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.DURATION));
            String stringType = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.TYPE));
            String simNumber = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));

            Date callDayTime = new Date(Long.valueOf(stringDate));
            int dircode = Integer.parseInt(stringType);
            String callType = "";
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    callType = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    callType = "MISSED";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    callType = "REJECTED";
                    break;
            }

            callLogsList.add(new CallLogsModel(stringNumber, stringName, callType, stringDuration, simNumber));

        } while (cursorCallLogs.moveToNext());
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        adapter = new CallLogsAdapter(getActivity(), callLogsList, new CallLogsAdapter.CallLogsAdapterCallbacks() {
            @Override
            public void onClick(String number) {
                showSpamAlert(number);
            }

            @Override
            public void onLongClick(String number) {
                showBlockAlert(number);

            }
        });
        recycler.setAdapter(adapter);


    }

    private void showBlockAlert(String number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage("Do you want to block this number? ");
        // add the buttons

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                blockNumber(number);
                CommonUtils.showToast("Blocked successfully");
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        dialog = builder.create();
        dialog.show();
    }

    private void showSpamAlert(String number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage("Do you want to mark as spam? ");
        // add the buttons

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                blockNumber(number);
                CommonUtils.showToast("Marked as spam successfully");
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        dialog = builder.create();
        dialog.show();
    }

    private void blockNumber(String number) {

        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        Call<ApiResponse> call = getResponse.blockNumber(number, "berer " + SharedPrefs.getToken());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getData() != null) {
//                        CommonUtils.showToast(response.body().getMessage());
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
                CommonUtils.showToast(t.getMessage());

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CALL_LOG
        };

        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        } else {
            readCallLogs();
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}
