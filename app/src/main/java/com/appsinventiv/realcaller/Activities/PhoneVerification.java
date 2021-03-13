package com.appsinventiv.realcaller.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

public class PhoneVerification extends AppCompatActivity {

    private CountryCodePicker ccp;
    private String foneCode;
    Button login;
    AppCompatEditText phone;
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        phone = findViewById(R.id.phone);
        login = findViewById(R.id.login);
        clear = findViewById(R.id.clear);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(PhoneVerification.this,OTPScreen.class));
//            }
//        });

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        foneCode = "+" + ccp.getDefaultCountryCode();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setText("");
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().length() == 0) {
                    phone.setError("Cant be empty");
                } else if (phone.getText().length() < 10 || phone.getText().length() > 12) {
                    phone.setError("Enter valid phone number");
                } else {
                    if (ConnectivityManagere.isNetworkConnected(PhoneVerification.this)) {
                        requestCode();
                    } else {
                        CommonUtils.showToast("Please check your internet connection");
                    }
                }
            }
        });
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                foneCode = "+" + selectedCountry.getPhoneCode();
            }
        });
        ccp.registerPhoneNumberTextView(phone);
    }

    private void requestCode() {
        String ph = phone.getText().toString();
        Intent i = new Intent(PhoneVerification.this, OTPScreen.class);
        i.putExtra("number", foneCode + ph);
        startActivity(i);


    }
}
