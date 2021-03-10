package com.appsinventiv.realcaller.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.realcaller.Adapters.MyProfileVIewsAdapter;
import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.NetworkResponses.ListModel;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.appsinventiv.realcaller.Utils.UserClient;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WhoViewedMyProfile extends AppCompatActivity {

    RecyclerView recycler;
    MyProfileVIewsAdapter adapter;
    private List<ListModel> itemList = new ArrayList<>();
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_viewd_my_profile);
        recycler = findViewById(R.id.recycler);
        progress = findViewById(R.id.progress);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Who viewed my profile");

        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        getDataFromServer();


    }

    private void getDataFromServer() {

        progress.setVisibility(View.VISIBLE);

        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        String token = SharedPrefs.getToken();
//         token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwNDUyMmRjY2VhMTgxMjYwY2NiZTM3YyIsImlhdCI6MTYxNTE0MzY0NH0.S6EYI33Mt5k1Qp0R32_l-AsGxdT06ywJBEsbDPAtJ2I";
        Call<ApiResponse> call = getResponse.myProfileViewers("berer " + token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.code() == 200) {
                    List<ListModel> data = response.body().getData().getList();

                    itemList = data;

                    adapter = new MyProfileVIewsAdapter(WhoViewedMyProfile.this, itemList);
                    recycler.setAdapter(adapter);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
