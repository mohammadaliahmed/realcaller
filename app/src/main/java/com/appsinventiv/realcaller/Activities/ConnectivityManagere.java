package com.appsinventiv.realcaller.Activities;

import android.content.Context;
import android.net.NetworkInfo;

public class ConnectivityManagere {
    public ConnectivityManagere() {
    }

    public static boolean isNetworkConnected(Context context) {
        android.net.ConnectivityManager cm =
                (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
