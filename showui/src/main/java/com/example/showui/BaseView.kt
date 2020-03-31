package com.example.showui

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator

open class BaseView : View {
    constructor(context: Context) : super(context) {
        initAnimation()
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr) {
        initAnimation()
    }

    private val defaultValue1 = 0.2f
    private val defaultValue2 = 1f
    private val defaultDuration = 2000L

    protected var mWidth = 0.0f
    protected var mHeight = 0.0f
    protected var scale = 0.3f

    protected var animator: ValueAnimator? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
    }

    private fun initAnimation() {
        animator = ValueAnimator.ofFloat(defaultValue1, defaultValue2)
        animator?.interpolator = BounceInterpolator()
        animator?.duration = defaultDuration
        animator?.addUpdateListener { animation ->
            scale = animation.animatedValue as Float
            postInvalidate()
        }
    }
}