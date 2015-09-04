package com.ionis.igem.app.utils;

import android.util.Log;

import java.util.Random;

/**
 * Created by PLNech on 28/08/2015.
 */
public class CalcUtils {
    private static final String TAG = "CalcUtils";

    private static Random random;
    
    public static int randomOf(int max) {
        if (random == null) {
            random = new Random();
        }
        return randomOf(max, random);
    }
    public static int randomOf(int max, Random random) {
        final int res = Math.abs(random.nextInt() % max);
        Log.d(TAG, "randomOf(" + max + ") -> " + res);
        return res;
    }
}
