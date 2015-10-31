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
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

class ThreadUtils {
    private static void runOnUiThread(Runnable runnable) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void toastOnUiThread(final Context context, final String message, final int length) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, length).show();
            }
        });
    }
}
