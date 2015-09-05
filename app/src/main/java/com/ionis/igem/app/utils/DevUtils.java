package com.ionis.igem.app.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by PLN on 14/08/2015.
 */
class DevUtils {

    public static void logToast(Context context, String message) {
        logToast(context, null, null, message);
    }

    public static void logToast(Context context, String TAG, String message) {
        logToast(context, TAG, null, message);
    }

    public static void logToast(Context context, String TAG, String methodName, String message) {
        if (TAG == null) {
            TAG = "DevLog";
        }
        if (methodName == null) {
            methodName = "logToast";
        }
        message = methodName + " - " + message;
        Log.d(TAG, message);
        ThreadUtils.toastOnUiThread(context, TAG + ": " + message, Toast.LENGTH_SHORT);
    }
}
