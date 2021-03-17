package com.appsinventiv.realcaller.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.Constants;
import com.appsinventiv.realcaller.Utils.KeyboardUtils;
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
    RelativeLayout asdasdas;
    LinearLayout searchLayout;
    CheckBox terms;
    Button submit;
    private boolean termsChecked;
    LinearLayout agreementLayout;

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
        agreementLayout = findViewById(R.id.agreementLayout);
        submit = findViewById(R.id.submit);
        terms = findViewById(R.id.terms);
        search = findViewById(R.id.search);
        asdasdas = findViewById(R.id.asdasdas);
        searchLayout = findViewById(R.id.searchLayout);
        number = findViewById(R.id.number);
        dataTv = findViewById(R.id.data);
        progress = findViewById(R.id.progress);
        number.setText(Constants.CALL_NUMBER);

        terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        termsChecked = true;
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termsChecked) {
                    SharedPrefs.setAgreement("done");
                    searchLayout.setVisibility(View.VISIBLE);
                    agreementLayout.setVisibility(View.GONE);
                } else {
                    CommonUtils.showToast("Please accept terms and conditions");
                }
            }
        });

//        dataTv.setText("Location: " + CommonUtils.getFullAddress(SearchNumber.this, 31.5325656,74.338289));


        if (SharedPrefs.getAgreement().equalsIgnoreCase("done")) {
            searchLayout.setVisibility(View.VISIBLE);
            agreementLayout.setVisibility(View.GONE);
        } else {
            searchLayout.setVisibility(View.GONE);
            agreementLayout.setVisibility(View.VISIBLE);
        }

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
        KeyboardUtils.forceCloseKeyboard(asdasdas);
        progress.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        String num = number.getText().toString();
//        num = "03075323974";

        String token = SharedPrefs.getToken();
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwNTFhNmZkYzM1NWI4MDAxNTYxOGFmYSIsImlhdCI6MTYxNTk2MzkwMX0.Nm231S8zGD4wCGeY037mRI35Im4p5j2bt86qBYSPjMA";

        Call<ApiResponse> call = getResponse.searchByPhone(num, true, "berer " + token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getData() != null) {
                        Data data = (Data) response.body().getData();
                        if (response.body().getData().getPhone().equalsIgnoreCase("")) {
                            CommonUtils.showToast(response.body().getMessage());
                        } else {
                            if (data.getLat() == 0) {
                                dataTv.setText("Name: " + data.getName() + "\nPhone: " + data.getPhone() + "\n\nLocation: Not found");
                            } else {
                                dataTv.setText("Name: " + data.getName() + "\nPhone: " + data.getPhone()
                                        + "\n\nCountry: " + CommonUtils.getCountry(SearchNumber.this, data.getLat(), data.getLon())
                                        + "\nState: " + CommonUtils.getState(SearchNumber.this, data.getLat(), data.getLon())
                                        + "\nCity: " + CommonUtils.getCity(SearchNumber.this, data.getLat(), data.getLon()));
                            }
                        }

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
