package com.wyc.anim.utils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.wyc.anim.utils.ItemTouchAnimUtils;


/**
 * 作者： wyc
 * <p>
 * 创建时间： 2021/5/26 10:09
 * <p>
 * 文件名字： com.wyc.vivodemo.widget
 * <p>
 * 类的介绍：
 */
public class ViewTouchAnimLayout extends FrameLayout {

    private boolean mChildViewsCanTouch = true;

    public ViewTouchAnimLayout(Context context) {
        this(context, null);
    }

    public ViewTouchAnimLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewTouchAnimLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        setFocusable(true);
    }

    /**
     * 设置该布局的所有子view是否能响应手势事件，例如touch、click
     *
     * @param canTouch
     */
    public void setChildViewsCanTouch(boolean canTouch) {
        mChildViewsCanTouch = canTouch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //事件拦截，返回true则拦截事件传递给子控件，即子控件无法进行touch和click等
        return !mChildViewsCanTouch || super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ItemTouchAnimUtils.startAnimDown(this);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ItemTouchAnimUtils.startAnimUp(this);
                break;
        }
        return super.onTouchEvent(event);
    }
}
