package com.wyc.popup.window.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;


public class KeyEventManager implements KeyEventInvocationHandler.KeyEventCallback {
    private static final String TAG = "KeyEventManager";
    private Context mContext;
    private KeyEventCallback mCallback;
    private TouchEventCallback mTouchEventCallBack;

    private static final String HOME_REASON = "homekey";
    private static final String RECENTAPPS_REASON = "recentapps";
    private boolean mRegisterTag;

    private static final int KEY_EVENT_HOME = 0;
    private static final int KEY_EVENT_RECENTAPPS = 1;
    private static final int KEY_EVENT_BACK = 2;
    private static final int EVENT_SCREEN_OFF = 3;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || mCallback == null) {
                return;
            }
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                mCallback.onKeyEvent(EVENT_SCREEN_OFF);
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                String reason = intent.getStringExtra("reason");
                Log.d(TAG, "onReceive: reason = " + reason);
                if (HOME_REASON.equals(reason)) {
                    mCallback.onKeyEvent(KEY_EVENT_HOME);
                } else if (RECENTAPPS_REASON.equals(reason)) {
                    mCallback.onKeyEvent(KEY_EVENT_RECENTAPPS);
                }
            }
        }
    };

    public KeyEventManager(Context context, KeyEventCallback callback, TouchEventCallback touchEventCallback) {
        mContext = context;
        mCallback = callback;
        mTouchEventCallBack = touchEventCallback;
        Log.d(TAG, "KeyEventManager");
        initReceiver();
    }

    public KeyEventManager(Context context) {
        this(context, null, null);
    }

    public void setKeyEventListener(KeyEventCallback callback) {
        this.mCallback = callback;
    }

    private synchronized void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        if (!mRegisterTag) {
            mRegisterTag = true;
            mContext.registerReceiver(mReceiver, filter);
        }
    }

    public synchronized void resetCallback() {
        mCallback = null;
        if (mReceiver != null && mRegisterTag) {
            try {
                mContext.unregisterReceiver(mReceiver);
                mReceiver = null;
                mRegisterTag = false;
            } catch (Exception e) {
                Log.v(TAG, "resetCallback ex:" + e.getMessage());
            }
        }
    }

    @Override
    public boolean onKeyEvent(InputEvent event) {
        if (mTouchEventCallBack != null) {
            mTouchEventCallBack.onTouchEvent(event);
        }
        if (event instanceof KeyEvent && mCallback != null) {
            KeyEvent keyEvent = (KeyEvent) event;
            Log.v(TAG, "onInputEvent event:" + keyEvent.getKeyCode());
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                return mCallback.onKeyEvent(KEY_EVENT_BACK);
            }
        }

        return false;
    }

    public interface KeyEventCallback {
        boolean onKeyEvent(int event);
    }

    public interface TouchEventCallback {
        void onTouchEvent(InputEvent event);
    }
}