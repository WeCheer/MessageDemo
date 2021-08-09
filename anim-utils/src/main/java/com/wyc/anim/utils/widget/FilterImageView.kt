package com.wyc.anim.utils.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class FilterImageView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

    private val mGestureDetector: GestureDetector = GestureDetector(context, this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_CANCEL
                || event.actionMasked == MotionEvent.ACTION_UP) {
            removeFilter()
        }
        return mGestureDetector.onTouchEvent(event)
    }

    private fun setFilter() {
        var drawable = drawable
        if (drawable == null) {
            drawable = background
        }
        drawable?.apply {
            val colorMatrix = ColorMatrix(floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 0.3f, 0f))
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }
    }

    private fun removeFilter() {
        var drawable = drawable
        if (drawable == null) {
            drawable = background
        }
        drawable?.apply {
            clearColorFilter()
        }
    }

    override fun onShowPress(e: MotionEvent?) {}

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        removeFilter()
        performClick()
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        setFilter()
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean = false

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false

    override fun onLongPress(e: MotionEvent?) {
        performLongClick()
    }
}