package com.example.whatstrending.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
