package com.appsinventiv.realcaller.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.appsinventiv.realcaller.Utils.UserClient;
import com.goodiebag.pinview.Pinview;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPScreen extends AppCompatActivity {

    ImageView back;
    TextView resend;
    Button verify;
    PhoneAuthProvider phoneAuth;
    Pinview pin;
    private String mVerificationId;

    String phoneNumber;
    private String smsCode;
    RelativeLayout wholeLayout;

    TextView phon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_screen);

        phon = findViewById(R.id.phon);
        wholeLayout = findViewById(R.id.wholeLayout);
        resend = findViewById(R.id.resend);
        pin = findViewById(R.id.pinview);
        back = findViewById(R.id.back);
        phoneNumber = getIntent().getStringExtra("number");
        if (phoneNumber.contains("9203")) {
            phoneNumber = phoneNumber.replace("9203", "923");
        }
        phon.setText("Enter your OTP code sent to your phone " + phoneNumber);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OTPScreen.this, Register.class));
            }
        });
        pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                callLoginApi();
            }
        });

//        requestCode();

    }

    private void callLoginApi() {
        wholeLayout.setVisibility(View.VISIBLE);

        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);

        JsonObject map = new JsonObject();
        map.addProperty("phone", phoneNumber);


        Call<ApiResponse> call = getResponse.login(map);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        Data data = (Data) response.body().getData();
                        if (data.getName().equalsIgnoreCase("")) {
                            Intent i = new Intent(OTPScreen.this, Register.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        } else {
                            SharedPrefs.setToken(data.getAccessToken());
                            SharedPrefs.setName(data.getName());
                            SharedPrefs.setPhone(phoneNumber);
                            Intent i = new Intent(OTPScreen.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
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

            }
        });
    }

    private void requestCode() {

        phoneAuth = PhoneAuthProvider.getInstance();

        phoneAuth.verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        smsCode = phoneAuthCredential.getSmsCode();
                        SharedPrefs.setPhone(phoneNumber);
                        if (phoneAuthCredential.getSmsCode() != null) {
                            pin.setValue(phoneAuthCredential.getSmsCode());
                        }


                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        CommonUtils.showToast(e.getMessage());

                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        CommonUtils.showToast("Code sent");
                        mVerificationId = verificationId;
                        // Save verification ID and resending token so we can use them later


                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        CommonUtils.showToast("Time out");
//                            sendCode.setText("Resend");
//                            progress.setVisibility(View.GONE);
                        finish();

                    }
                }
        );
    }
}
