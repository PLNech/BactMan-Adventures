/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/
package fr.plnech.igem.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class DevUtils {

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

    public static void openLink(String url, Context context) {
        Intent iWebsite = new Intent(Intent.ACTION_VIEW);
        iWebsite.setData(Uri.parse(url));
        context.startActivity(iWebsite);
    }

    public static void openFacebookLink(String url, ContextWrapper contextWrapper) {
        openFacebookLink(url, contextWrapper.getPackageManager(), contextWrapper);
    }

    public static void openFacebookLink(String url, PackageManager pm, Context context) {
        Uri uri;
        try {
            pm.getPackageInfo("com.facebook.katana", 0);
            // http://stackoverflow.com/a/24547437/1048340
            uri = Uri.parse("fb://facewebmodal/f?href=" + url);
        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse(url);
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public static void openLinkedinLink(String id, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://" + id));
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + id));
        }
        context.startActivity(intent);
    }
}
