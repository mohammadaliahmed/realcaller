package com.appsinventiv.realcaller.Activities.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.realcaller.Activities.DialerActivity;
import com.appsinventiv.realcaller.Adapters.CallLogsAdapter;
import com.appsinventiv.realcaller.Adapters.SmsAdapter;
import com.appsinventiv.realcaller.Models.CallLogsModel;
import com.appsinventiv.realcaller.Models.SmsModel;
import com.appsinventiv.realcaller.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class SmsFragment extends Fragment {
    private View rootView;

    FloatingActionButton fab2, fab3, fab4;
    boolean flag = true;
    TextView text;
    private List<SmsModel> smsList = new ArrayList<>();
    HashMap<String, SmsModel> smsMap = new HashMap<>();
    RecyclerView recycler;
    SmsAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sms_fragment, container, false);
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
    }

    private void readSMS() {


        ContentResolver cr = getContext().getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    Date dateFormat = new Date(Long.valueOf(smsDate));
                    String type = "";
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            type = "sent";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            type = "outbox";
                            break;
                        default:
                            break;
                    }
                    if (!smsMap.containsKey(number)) {
                        smsMap.put(number, new SmsModel(number, "", type, body, "" + smsDate));
                    }
//                    smsList.add(new SmsModel(number, "", type, body, "" + dateFormat));
                    c.moveToNext();
                }
            }

            c.close();


        }

        smsList = new ArrayList<>(smsMap.values());
        Collections.sort(smsList, new Comparator<SmsModel>() {
            @Override
            public int compare(SmsModel listData, SmsModel t1) {
                Long ob1 = Long.parseLong(listData.getDate());
                Long ob2 = Long.parseLong(t1.getDate());
                return ob2.compareTo(ob1);

            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        adapter = new SmsAdapter(getActivity(), smsList, new SmsAdapter.SmsAdapterCallbacks() {
            @Override
            public void onClick(String number) {

            }
        });
        recycler.setAdapter(adapter);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_SMS
        };

        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        } else {
            readSMS();
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}
