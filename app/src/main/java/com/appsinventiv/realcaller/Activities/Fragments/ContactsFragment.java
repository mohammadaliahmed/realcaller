package com.appsinventiv.realcaller.Activities.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.realcaller.Adapters.ContactsAdapter;
import com.appsinventiv.realcaller.Models.ContactModel;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class ContactsFragment extends Fragment {
    private View rootView;
    RecyclerView recycler;
    EditText search;
    ContactsAdapter adapter;
    private List<ContactModel> contactList = new ArrayList<>();
    HashMap<String, ContactModel> hashMap = new HashMap<>();
    HashMap<String, String> contactsMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contacts_fragment, container, false);
        search = rootView.findViewById(R.id.search);
        recycler = rootView.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new ContactsAdapter(getContext(), contactList);
        recycler.setAdapter(adapter);
        getPermissions();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });

        return rootView;

    }

    private void getDataFromDB() {
        contactList.clear();
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("-", "");
            hashMap.put(name, new ContactModel(phoneNumber, name));
            contactsMap.put(phoneNumber, name);


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

        adapter.updateList(contactList);
        SharedPrefs.setContactsMap(contactsMap);


    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
        };

        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
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

}
