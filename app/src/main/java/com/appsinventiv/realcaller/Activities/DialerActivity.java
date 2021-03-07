package com.appsinventiv.realcaller.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.realcaller.Adapters.CallLogsAdapter;
import com.appsinventiv.realcaller.Adapters.ContactsAdapter;
import com.appsinventiv.realcaller.Adapters.ContactsHorizontalAdapter;
import com.appsinventiv.realcaller.Models.CallLogsModel;
import com.appsinventiv.realcaller.Models.ContactModel;
import com.appsinventiv.realcaller.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DialerActivity extends AppCompatActivity {


    LinearLayout click0, click1, click2, click3, click4, click5, click6, click7, click8, click9, clickStar, clickHash;
    ImageView addContact, erase, call, closeKeyboard;
    LinearLayout phonePad;
    boolean phonePadVisible = true;
    EditText number;

    String numberString = "";
    ImageView close;
    RecyclerView contactsRecycler, logsRecycler;
    ContactsHorizontalAdapter contactsHorizontalAdapter;
    private ArrayList<ContactModel> contactList = new ArrayList<>();
    private HashMap<String, ContactModel> hashMap = new HashMap<>();
    private List<CallLogsModel> callLogsList = new ArrayList<>();
    private CallLogsAdapter callLogsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);

        contactsRecycler = findViewById(R.id.contactsRecycler);
        logsRecycler = findViewById(R.id.logsRecycler);
        close = findViewById(R.id.close);
        initPhonePad();
        getPermissions();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void readCallLogs() {

        Uri uriCallLogs = Uri.parse("content://call_log/calls");
        Cursor cursorCallLogs = getContentResolver().query(uriCallLogs, null, null, null);
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
        logsRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        callLogsAdapter = new CallLogsAdapter(this, callLogsList, new CallLogsAdapter.CallLogsAdapterCallbacks() {
            @Override
            public void onClick(String numb) {
                numberString = numb;
                number.setText(numberString);
            }
        });
        logsRecycler.setAdapter(callLogsAdapter);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataFromDB() {
        contactList.clear();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            hashMap.put(name, new ContactModel(phoneNumber, name));


        }
        phones.close();

        contactList = new ArrayList<ContactModel>(hashMap.values());

        Collections.sort(contactList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel listData, ContactModel t1) {
                String ob1 = listData.getName();
                String ob2 = t1.getName();
                return ob1.compareTo(ob2);

            }
        });
        contactsRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        contactsHorizontalAdapter = new ContactsHorizontalAdapter(this, contactList, new ContactsHorizontalAdapter.ContactsHoriCallbacks() {
            @Override
            public void onClick(String numb) {
                numberString = numb;
                number.setText(numberString);
            }
        });
        contactsRecycler.setAdapter(contactsHorizontalAdapter);
        contactsHorizontalAdapter.updateList(contactList);
        readCallLogs();


    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CALL_LOG

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            getDataFromDB();

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

    private void initPhonePad() {
        number = findViewById(R.id.number);
        click0 = findViewById(R.id.click0);
        click1 = findViewById(R.id.click1);
        click2 = findViewById(R.id.click2);
        click3 = findViewById(R.id.click3);
        click4 = findViewById(R.id.click4);
        click5 = findViewById(R.id.click5);
        click6 = findViewById(R.id.click6);
        click7 = findViewById(R.id.click7);
        click8 = findViewById(R.id.click8);
        click9 = findViewById(R.id.click9);
        clickStar = findViewById(R.id.clickStar);
        clickHash = findViewById(R.id.clickHash);
        addContact = findViewById(R.id.addContact);
        erase = findViewById(R.id.erase);
        call = findViewById(R.id.call);
        phonePad = findViewById(R.id.phonePad);
        closeKeyboard = findViewById(R.id.closeKeyboard);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLastChars(numberString, 1);

            }
        });


        click0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "0";
                number.setText(numberString);
            }
        });
        click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "1";
                number.setText(numberString);
            }
        });
        click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "2";
                number.setText(numberString);
            }
        });
        click3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "3";
                number.setText(numberString);
            }
        });
        click4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "4";
                number.setText(numberString);
            }
        });
        click5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "5";
                number.setText(numberString);
            }
        });
        click6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "6";
                number.setText(numberString);
            }
        });
        click7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "7";
                number.setText(numberString);
            }
        });
        click8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "8";
                number.setText(numberString);
            }
        });
        click9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "9";
                number.setText(numberString);
            }
        });
        clickStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "*";
                number.setText(numberString);
            }
        });
        clickHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberString = numberString + "#";
                number.setText(numberString);
            }
        });


        closeKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phonePadVisible) {
                    phonePadVisible = false;
                    phonePad.setVisibility(View.GONE);

                } else {
                    phonePad.setVisibility(View.VISIBLE);
                    phonePadVisible = true;
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() == 0) {

                } else {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number.getText().toString()));
                    startActivity(i);
                }
            }
        });
    }


    public String removeLastChars(String str, int chars) {
        numberString = str.substring(0, str.length() - chars);
        number.setText(numberString);
        return numberString;
    }

}
