package com.acmvit.c2c2021.ui

import android.R
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat

class OverlayFrame : CoordinatorLayout {
    private var overlay = false
    private var plainDrawable: ColorDrawable? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun displayOverlay(isVisible: Boolean, overlayDrawable: ColorDrawable) {
        overlay = isVisible
        plainDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        foreground = if (isVisible) {
            overlayDrawable
        } else {
            plainDrawable
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return overlay
    }
}