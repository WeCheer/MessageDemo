package com.wyc.popup.window.manager;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneManager {
    private static final String TAG = "PhoneManager";

    private Context mContext;
    private TelephonyManager mTelephonyManager = null;
    private PhoneStateCallback mCallback;

    public PhoneManager(Context context, PhoneStateCallback callback) {
        mContext = context;
        mCallback = callback;
        registerListener();
    }

    private void registerListener() {
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void unRegisterListener() {
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(mPhoneStateListener, 0);
        }
    }

    public void resetCallback() {
        mCallback = null;
        unRegisterListener();
    }

    /**
     * 返回当前通话状态
     * */
    public int getCallState() {
        if (mTelephonyManager == null) {
            return TelephonyManager.CALL_STATE_IDLE;
        }
        return mTelephonyManager.getCallState();
    }

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.v(TAG, "onCallStateChanged " + state);
            if (state != TelephonyManager.CALL_STATE_IDLE && mCallback != null ) {
                mCallback.onPhoneRinging();
            }
        }
    };

    public interface PhoneStateCallback {
        void onPhoneRinging();
    }
}
