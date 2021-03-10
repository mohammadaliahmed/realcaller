package com.appsinventiv.realcaller.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.Constants;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.appsinventiv.realcaller.Utils.UserClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchNumber extends AppCompatActivity {

    EditText number;
    ImageView search;
    TextView dataTv;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Search Location");
        search = findViewById(R.id.search);
        number = findViewById(R.id.number);
        dataTv = findViewById(R.id.data);
        progress = findViewById(R.id.progress);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 10) {
                    number.setError("Enter correct number");
                } else {
                    performSearch();
                }
            }
        });


    }

    private void performSearch() {
        progress.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        Call<ApiResponse> call = getResponse.searchByPhone(number.getText().toString(), "berer " + SharedPrefs.getToken());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getData() != null) {
                        Data data = (Data) response.body().getData();
                        dataTv.setText(data.getName() + "\n" + data.getPhone());
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
