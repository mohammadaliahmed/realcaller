package com.appsinventiv.realcaller.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.realcaller.Models.FacebookProfileModel;
import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.appsinventiv.realcaller.NetworkResponses.Data;
import com.appsinventiv.realcaller.R;
import com.appsinventiv.realcaller.Utils.AppConfig;
import com.appsinventiv.realcaller.Utils.CommonUtils;
import com.appsinventiv.realcaller.Utils.SharedPrefs;
import com.appsinventiv.realcaller.Utils.UserClient;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText phone, name, email, password, confirmPassword;
    ImageView back;
    Button register;
    RelativeLayout wholeLayout;
    TextView login;
    public static FacebookProfileModel profile;
    LoginButton facebook;
    ImageView fbImg;
    private CallbackManager mCallbackManager;
    public static GoogleSignInAccount account;
    private boolean social;
    GoogleApiClient apiClient;
    ImageView google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        facebook = findViewById(R.id.facebook);
        google = findViewById(R.id.google);
        login = findViewById(R.id.login);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        back = findViewById(R.id.back);
        wholeLayout = findViewById(R.id.wholeLayout);
        register = findViewById(R.id.register);
        fbImg = findViewById(R.id.fbImg);

        facebook.setPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        printHashKey(this);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        fbImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Register.this, Arrays.asList("email", "public_profile"));
                facebook.performClick();
                wholeLayout.setVisibility(View.VISIBLE);
            }
        });

        phone.setText(SharedPrefs.getPhone());
//        phone.setText("+923158000333");

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
        facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    Register.account = null;

                                    String emaill = object.getString("email");
                                    String firstName = object.getString("name").split(" ")[0];
                                    String lastName = object.getString("name").split(" ")[1];
                                    String id = object.getString("id");
                                    profile = new FacebookProfileModel(id, firstName, lastName, emaill);

                                    social = true;

//                                    loginUser(firstName+lastName);
                                    email.setText(emaill);
                                    name.setText(firstName + " " + lastName);
                                    password.setText(id);
                                    confirmPassword.setText(id);
                                    String userOd = profile.getFirstName() + profile.getLastName();
                                    callLoginApi();
//                                    if (map.containsKey(userOd)) {
//                                        callLoginApi(userOd);
//                                        LoginManager.getInstance().logOut();
//                                    } else {
//                                        Intent i = new Intent(LoginMenu.this, Register.class);
//                                        i.putExtra("userId", profile.getFirstName() + profile.getLastName());
//                                        i.putExtra("email", profile.getId());
//                                        startActivity(i);
                                    LoginManager.getInstance().logOut();
//                                    }


                                } catch (Exception e) {
                                    CommonUtils.showToast(e.getMessage());
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

//                profile = Profile.getCurrentProfile();
//
//                String userOd = profile.getFirstName() + profile.getLastName();
//                if (map.containsKey(userOd)) {
//                    loginUser(userOd);
//                    LoginManager.getInstance().logOut();
//                } else {
//                    Intent i = new Intent(LoginMenu.this, Register.class);
//                    i.putExtra("userId", profile.getFirstName() + profile.getLastName());
//                    i.putExtra("email", profile.getId());
//                    startActivity(i);
//                    LoginManager.getInstance().logOut();
//                }
            }

            @Override
            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
//                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                CommonUtils.showToast(error.getMessage());
//                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
//                updateUI(null);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]


    }

    private void signin() {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(i, 100);
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("hashkey", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("hashkey", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("hashkey", "printHashKey()", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleResult(GoogleSignInResult googleSignInResult) {
        wholeLayout.setVisibility(View.VISIBLE);
        if (googleSignInResult.isSuccess()) {

            account = googleSignInResult.getSignInAccount();
//            e_fullname.setText(account.getDisplayName());
            name.setText(account.getDisplayName());
            email.setText(account.getEmail());
            password.setText(account.getId());
            confirmPassword.setText(account.getId());
            String userId = account.getEmail().replace("@", "").replace(".", "");
            String email = account.getEmail();
            social = true;
            callLoginApi();

            Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                }
            });

        } else {
            wholeLayout.setVisibility(View.GONE);
            CommonUtils.showToast("There some error");
        }

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
                        Data data = (Data) response.body().getData();
                        SharedPrefs.setToken(data.getAccessToken());
                        SharedPrefs.setName(data.getName());
                        SharedPrefs.setName(data.getEmail());
                        SharedPrefs.setPhone(data.getPhone());
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
                        if (social) {
                            if (jObjError.getString("message").contains("Please signup first")) {
                                registerNow();
                            } else {
                                CommonUtils.showToast(jObjError.getString("message").toString());
                            }
                        } else {
                            CommonUtils.showToast(jObjError.getString("message").toString());
                        }
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
