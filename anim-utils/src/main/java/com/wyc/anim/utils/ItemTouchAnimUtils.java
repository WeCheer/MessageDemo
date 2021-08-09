package com.wyc.anim.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.view.animation.PathInterpolator;

public class ItemTouchAnimUtils {

    private static final String TAG = "ItemAnimUtils";
    private static final int ANIM_DOWN_SPEED = 250;
    private static final int ANIM_UP_SPEED = 300;

    /**
     * 变速器
     */
    private static final PathInterpolator INTERPOLATOR = new PathInterpolator(
            BezierUtils.buildPath(new PointF(0.25f, 0.1f), new PointF(0.25f, 1f))
    );

    /**
     * 按下动画
     *
     * @param view 执行动画的View
     */
    public static void startAnimDown(View view) {
        if (!view.isClickable()) {
            return;
        }
        try {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
                    .setDuration(ANIM_DOWN_SPEED);
            animator.setInterpolator(INTERPOLATOR);
            animator.start();
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    /**
     * 抬起动画
     *
     * @param view 执行动画的View
     */
    public static void startAnimUp(View view) {
        if (!view.isClickable()) {
            Log.w(TAG, "view is not clickable");
            return;
        }
        try {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1f);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).setDuration(ANIM_UP_SPEED);
            animator.setInterpolator(INTERPOLATOR);
            animator.start();
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }
}
