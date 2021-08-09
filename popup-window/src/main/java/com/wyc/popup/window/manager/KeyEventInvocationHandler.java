package com.wyc.popup.window.manager;

import android.view.InputEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class KeyEventInvocationHandler implements InvocationHandler {
    private KeyEventCallback mCallback;

    public KeyEventInvocationHandler(KeyEventCallback callback) {
        mCallback = callback;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (mCallback != null && args != null && args.length == 1 && args[0] instanceof InputEvent) {
            return mCallback.onKeyEvent((InputEvent) args[0]);
        }
        return false;
    }

    public interface KeyEventCallback {
        boolean onKeyEvent(InputEvent event);
    }
}
