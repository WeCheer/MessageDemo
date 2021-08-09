package com.wyc.message;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * 作者： wyc
 * <p>
 * 创建时间： 2021/5/26 11:38
 * <p>
 * 文件名字： com.wyc.vivodemo
 * <p>
 * 类的介绍：
 */
public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static App sInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sContext = this.getApplicationContext();
    }

    public static App getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sContext;
    }
}
