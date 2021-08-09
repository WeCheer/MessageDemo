package com.wyc.message.manager

import android.app.Activity
import android.content.Context
import android.view.*
import android.widget.PopupWindow
import androidx.annotation.FloatRange

class PopupWindowHelper private constructor(val context: Context) : PopupWindow.OnDismissListener {

    private var mPopupWindow: PopupWindow? = null
    private var mContentView: View? = null
    private var mWindow: Window? = null
    //窗口的宽
    private var mWidth = 0
    //窗口的高
    private var mHeight = 0
    //动画
    private var mAnimationStyle = -1
    //获取焦点
    private var mFocusable = true
    //点击外部是否消失
    private var mOutsideCancel = true
    //弹出PopWindow 背景是否变暗，默认不会变暗。
    private var mIsBackgroundDark = false
    // 背景变暗的值，0 - 1
    private var mBackgroundDarkValue = 0f
    //触摸事件拦截
    private var mTouchable = true //default is true
    private var mOnTouchListener: View.OnTouchListener? = null
    private var mDismissListener: PopupWindow.OnDismissListener? = null

    private fun create(): PopupWindow {
        setBackgroundParams()
        mPopupWindow = if (mWidth != 0 && mHeight != 0) {
            PopupWindow(mContentView, mWidth, mHeight)
        } else {
            PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        mPopupWindow?.apply {
            if (mAnimationStyle != -1) {
                mPopupWindow?.animationStyle = mAnimationStyle
            }
            apply(this)

            if (mWidth == 0 || mHeight == 0) {
                contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                //如果外面没有设置宽高的情况下，计算宽高并赋值
                mWidth = contentView.measuredWidth
                mHeight = contentView.measuredHeight
            }
            // 添加dismiss 监听
            setOnDismissListener(this@PopupWindowHelper)
            isFocusable = mFocusable
            setBackgroundDrawable(null)
            isOutsideTouchable = mOutsideCancel
        }
        return mPopupWindow!!
    }

    private fun setBackgroundParams() {
        val activity = mContentView?.context as? Activity
        if (activity != null && mIsBackgroundDark) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            val alpha = if (mBackgroundDarkValue in 0.0..1.0) {
                mBackgroundDarkValue
            } else {
                0f
            }
            mWindow = activity.window
            mWindow?.let {
                val params: WindowManager.LayoutParams = it.attributes
                params.alpha = alpha
                it.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                it.attributes = params
            }
        }
    }

    private fun apply(popupWindow: PopupWindow) {
        if (mDismissListener != null) {
            popupWindow.setOnDismissListener(mDismissListener)
        }
        if (mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(mOnTouchListener)
        }
        popupWindow.isTouchable = mTouchable
    }


    fun getPopupWindow(): PopupWindow? {
        return mPopupWindow
    }

    fun getWidth() = mWidth

    fun getHeight() = mHeight

    override fun onDismiss() {
        dismiss()
    }

    /**
     * popup 消失
     */
    fun dismiss() {
        mDismissListener?.onDismiss()
        // 如果设置了背景变暗，那么在dismiss的时候需要还原
        mWindow?.let {
            val params: WindowManager.LayoutParams = it.attributes
            params.alpha = 1.0f
            it.attributes = params
        }

        if (mPopupWindow?.isShowing == true) {
            mPopupWindow?.dismiss()
        }
    }

    /**
     * 相对于窗体的显示位置
     *
     * @param view    可以为Activity中的任意一个View（最终的效果一样），会通过这个View找到其父Window，也就是Activity的Window。
     * @param gravity 在窗体中的位置，默认为Gravity.NO_GRAVITY
     * @param x       表示距离Window边缘的距离，方向由Gravity决定。
     * 例如：设置了Gravity.TOP，则y表示与Window上边缘的距离；
     * 而如果设置了Gravity.BOTTOM，则y表示与下边缘的距离。
     * @param y
     * @return
     */
    fun showAtLocation(view: View, x: Int, y: Int, gravity: Int): PopupWindowHelper {
        mPopupWindow?.showAtLocation(view, gravity, x, y)
        return this
    }

    /**
     * 显示在anchor控件的正下方，或者相对这个控件的位置
     *
     * @param anchor
     * @param xOffset
     * @param yOffset
     * @param gravity
     * @return
     */
    @JvmOverloads
    fun showAsDropDown(anchor: View, xOffset: Int = 0, yOffset: Int = 0, gravity: Int = Gravity.TOP or Gravity.START): PopupWindowHelper {
        mPopupWindow?.showAsDropDown(anchor, xOffset, yOffset, gravity)
        return this
    }

    /**
     * 根据id获取view
     *
     * @param viewId
     * @return
     */
    fun getItemView(viewId: Int): View? {
        return mContentView?.findViewById(viewId)
    }

    /**
     * 根据id设置pop内部的控件的点击事件的监听
     *
     * @param viewId
     * @param listener
     */
    fun setOnMenuClickListener(viewId: Int, listener: View.OnClickListener?): PopupWindowHelper {
        val view = getItemView(viewId)
        view?.setOnClickListener(listener)
        return this
    }


    fun setOnDismissListener(listener: PopupWindow.OnDismissListener?): PopupWindowHelper {
        this.mDismissListener = listener
        return this
    }

    fun setTouchInterceptor(touchInterceptor: View.OnTouchListener?): PopupWindowHelper {
        this.mOnTouchListener = touchInterceptor
        return this
    }


    /**
     * builder 类
     */
    class Builder(private val mContext: Context) {

        private val mHelper: PopupWindowHelper = PopupWindowHelper(mContext)

        fun setContentView(contentViewId: Int): Builder {
            return setContentView(LayoutInflater.from(mContext).inflate(contentViewId, null))
        }

        fun setContentView(view: View): Builder {
            mHelper.mContentView = view
            return this
        }

        fun setWidth(width: Int): Builder {
            mHelper.mWidth = width
            return this
        }

        fun setHeight(height: Int): Builder {
            mHelper.mHeight = height
            return this
        }

        fun setAnimationStyle(animStyle: Int): Builder {
            mHelper.mAnimationStyle = animStyle
            return this
        }

        fun setFocusable(focusable: Boolean): Builder {
            mHelper.mFocusable = focusable
            return this
        }

        fun setOutSideCancel(outsideCancel: Boolean): Builder {
            mHelper.mOutsideCancel = outsideCancel
            return this
        }

        /**
         * 设置背景变暗是否可用
         * @param isDark
         * @return
         */
        fun enableBackgroundDark(isDark: Boolean): Builder {
            mHelper.mIsBackgroundDark = isDark
            return this
        }

        /**
         * 设置背景变暗的值
         * @param darkValue
         * @return
         */
        fun setBackgroundDarkAlpha(@FloatRange(from = 0.0, to = 1.0) darkValue: Float): Builder {
            mHelper.mBackgroundDarkValue = darkValue
            return this
        }

        fun setTouchable(touchable: Boolean): Builder {
            mHelper.mTouchable = touchable
            return this
        }


        fun build(): PopupWindowHelper {
            mHelper.create()
            return mHelper
        }
    }
}