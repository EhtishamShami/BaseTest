package com.vophamtuananh.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by vophamtuananh on 10/26/17.
 */

public class NetworkUtil {

    @SuppressLint("MissingPermission")
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return null != activeNetwork && activeNetwork.isConnected();
    }

}
