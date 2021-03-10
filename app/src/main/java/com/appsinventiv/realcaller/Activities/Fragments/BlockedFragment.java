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
import com.appsinventiv.realcaller.Activities.Login;
import com.appsinventiv.realcaller.Activities.MainActivity;
import com.appsinventiv.realcaller.Activities.SearchNumber;
import com.appsinventiv.realcaller.Adapters.BlockedAdapter;
import com.appsinventiv.realcaller.Adapters.CallLogsAdapter;
import com.appsinventiv.realcaller.Models.CallLogsModel;
import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.NetworkResponses.ListModel;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.appsinventiv.realcaller.Utils.UserClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BlockedFragment extends Fragment {
    private View rootView;

    FloatingActionButton fab2, fab3, fab4;
    boolean flag = true;
    TextView text;
    RecyclerView recycler;
    BlockedAdapter adapter;
    private List<ListModel> blockedList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.call_logs_fragment, container, false);
        recycler = rootView.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        setupFloating();
        getDataFromServer();

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

    public void getDataFromServer() {
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        String token=SharedPrefs.getToken();
//         token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwNDI3M2YyOTZjNWRkMDVjNDBiMDBmYiIsImlhdCI6MTYxNDk2Nzc5OH0.C4rY9GdePnA6MKuAqc0BhPzGbWHP0FwoseigdBiS1LY";
        Call<ApiResponse> call = getResponse.getblockedContacts("berer " + token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        blockedList = response.body().getData().getList();
                        adapter = new BlockedAdapter(getActivity(), blockedList);
                        recycler.setAdapter(adapter);
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


}
