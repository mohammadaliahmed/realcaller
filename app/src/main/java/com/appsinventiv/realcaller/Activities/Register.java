package com.appsinventiv.realcaller.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.appsinventiv.realcaller.Utils.UserClient;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    EditText phone, name, email, password, confirmPassword;
    ImageView back;
    Button register;
    RelativeLayout wholeLayout;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = findViewById(R.id.login);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        back = findViewById(R.id.back);
        wholeLayout = findViewById(R.id.wholeLayout);
        register = findViewById(R.id.register);

        phone.setText(SharedPrefs.getPhone());
//        phone.setText("+9203158000333");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() == 0) {
                    name.setError("Enter name");
                } else if (email.getText().length() == 0) {
                    email.setError("Enter email");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter password");
                } else if (confirmPassword.getText().length() == 0) {
                    confirmPassword.setError("Enter confirm password");
                } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                    CommonUtils.showToast("Password does not match");
                } else {
                    registerNow();
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void registerNow() {
        wholeLayout.setVisibility(View.VISIBLE);

        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        JsonObject map = new JsonObject();
        map.addProperty("name", name.getText().toString());
        map.addProperty("phone", phone.getText().toString());
        map.addProperty("email", email.getText().toString());
        map.addProperty("password", password.getText().toString());


        Call<ApiResponse> call = getResponse.signup(map);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        callLoginApi();
                    } else {
                        CommonUtils.showToast(response.body().getMessage());
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtils.showToast(jObjError.get("message").toString());
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

    private void callLoginApi() {
        wholeLayout.setVisibility(View.VISIBLE);

        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        JsonObject map = new JsonObject();
        map.addProperty("email", email.getText().toString());
        map.addProperty("password", password.getText().toString());


        Call<ApiResponse> call = getResponse.login(map);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        SharedPrefs.setToken(response.body().getData().getAccessToken());
                        Intent i = new Intent(Register.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
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
