package com.wyc.message.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.wyc.message.App;


public class ScreenUtils {
    private static final String TAG = "ScreenUtils";
    private static int sScreenWidth = 0;
    private static int sScreenHeight = 0;
    private static int sDensityDpi = 0;

    public static int getScreenWidth() {
        if (sScreenWidth == 0) {
            init();
        }
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        if (sScreenHeight == 0) {
            init();
        }
        return sScreenHeight;
    }

    public static int getDensityDpi() {
        if (sDensityDpi == 0) {
            init();
        }
        return sDensityDpi;
    }


    private static void init() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) App.getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getRealMetrics(metrics);
        sScreenWidth = metrics.widthPixels;
        sScreenHeight = metrics.heightPixels;
        if (sScreenWidth > sScreenHeight) {
            int temp = sScreenWidth;
            sScreenWidth = sScreenHeight;
            sScreenHeight = temp;
        }
        sDensityDpi = metrics.densityDpi;
    }


    public static void keepScreenOn(Activity activity, boolean on) {
        if (on) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public static boolean isScreenLocked(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.isKeyguardLocked();
    }

    public static boolean isScreenOrientationPortrait() {
        Context context = App.getContext();
        if (context == null || context.getResources() == null) {
            return false;
        }
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration == null) {
            return false;
        }
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
