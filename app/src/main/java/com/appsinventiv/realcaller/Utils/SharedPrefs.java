package com.appsinventiv.realcaller.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.appsinventiv.realcaller.Models.ContactModel;
import com.appsinventiv.realcaller.Models.SmsModel;
import com.appsinventiv.realcaller.Models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

/**
 * Created by AliAh on 29/03/2018.
 */

public class SharedPrefs {
    Context context;

    private SharedPrefs() {

    }

    public static void setFavouriteMsgs(List<SmsModel> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("setFavouriteMsgs", json);
    }

    public static List<SmsModel> getFavouriteMsgs() {
        Gson gson = new Gson();
        List<SmsModel> playersList = (List<SmsModel>) gson.fromJson(preferenceGetter("setFavouriteMsgs"),
                new TypeToken<List<SmsModel>>() {
                }.getType());
        return playersList;
    }

    public static void setContactsMap(HashMap<String, String> itemList) {

        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        preferenceSetter("setContactsMap", json);
    }

    public static HashMap<String, String> getContactsMap() {
        Gson gson = new Gson();

        HashMap<String, String> retMap = new Gson().fromJson(
                preferenceGetter("setContactsMap"), new TypeToken<HashMap<String, String>>() {
                }.getType()
        );

        return retMap;
    }


    public static String getName() {
        return preferenceGetter("getName");
    }

    public static void setName(String username) {
        preferenceSetter("getName", username);
    }

    public static String getToken() {
        return preferenceGetter("bearer");
    }

    public static void setToken(String username) {
        preferenceSetter("bearer", username);
    }

    public static String getPhone() {
        return preferenceGetter("getPhone");
    }

    public static void setPhone(String username) {
        preferenceSetter("getPhone", username);
    }

    public static String getEmail() {
        return preferenceGetter("getEmail");
    }

    public static void setAgreement(String username) {
        preferenceSetter("setAgreement", username);
    }

    public static String getAgreement() {
        return preferenceGetter("setAgreement");
    }

    public static void setEmail(String username) {
        preferenceSetter("getEmail", username);
    }

    public static String getFcmKey() {
        return preferenceGetter("getFcmKey");
    }

    public static void setFcmKey(String username) {
        preferenceSetter("getFcmKey", username);
    }

    public static String getLat() {
        return preferenceGetter("getLat");
    }

    public static void setLat(String username) {
        preferenceSetter("getLat", username);
    }

    public static String getLon() {
        return preferenceGetter("getLon");
    }

    public static void setLon(String username) {
        preferenceSetter("getLon", username);
    }


    public static void setUser(User model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("customerModel", json);
    }

    public static User getUser() {
        Gson gson = new Gson();
        User customer = gson.fromJson(preferenceGetter("customerModel"), User.class);

        return customer;
    }

    public static void setTempUser(User model) {

        Gson gson = new Gson();
        String json = gson.toJson(model);
        preferenceSetter("setTempUser", json);
    }

    public static User getTempUser() {
        Gson gson = new Gson();
        User customer = gson.fromJson(preferenceGetter("setTempUser"), User.class);

        return customer;
    }

    public static void preferenceSetter(String key, String value) {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String preferenceGetter(String key) {
        SharedPreferences pref;
        String value = "";
        pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        value = pref.getString(key, "");
        return value;
    }

    public static void clearApp() {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        pref.edit().clear().commit();


    }

    public static void logout() {
        SharedPreferences pref = ApplicationClass.getInstance().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
