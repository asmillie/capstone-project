package com.example.whatstrending.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.whatstrending.R;

public class NetworkUtils {

    /** Tag for Error Logging **/
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private NetworkUtils() {}

    public static Boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork;
        boolean isConnected = false;
        try {
            activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnected();
        } catch (NullPointerException e) {
            Log.e(TAG, "Error retrieving active network info");
        }

        return isConnected;
    }
}
