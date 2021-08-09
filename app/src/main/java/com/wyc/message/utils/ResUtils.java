package com.wyc.message.utils;

import com.wyc.message.App;

import androidx.core.content.ContextCompat;

public class ResUtils {
    public static int getDimen(int dimenId) {
        return App.getContext().getResources().getDimensionPixelSize(dimenId);
    }

    public static String getString(int strId) {
        return App.getContext().getString(strId);
    }

    public static int getColor(int colorId) {
        return ContextCompat.getColor(App.getContext(), colorId);
    }
}
