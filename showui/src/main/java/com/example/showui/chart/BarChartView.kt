package com.example.showui.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.example.showui.BaseView
import com.example.showui.DensityUtil
import com.example.showui.model.BarBean
import java.util.ArrayList
import kotlin.math.ceil

class BarChartView : BaseView, View.OnTouchListener {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private var mBarPaint: Paint? = null

    private var mData: List<Float>? = null

    private var mDescription: List<String>? = null

    private var mBarShowNum = 6
    private val mMinBarScrollShowNum = 5

    private var mBarSingleWidth = 0f
    private var mBarMaxHeight = 0f
    private var mBarMaxWidth = 0f
    private var mBarBlankSize = 0f
    private var mBarCornerSize = 20f

    private var mHeightBlankSize = 0f
    private var mTextHeight = 0f

    private val mTextBounds = Rect()

    private var mMaxData = 0f

    private var shader: LinearGradient? = null

    private var detector: GestureDetector? = null

    private var offset = 0f

    private var mColor: Int = Color.RED

    private fun init() {
        mData = ArrayList()
        mDescription = ArrayList()

        mBarPaint = Paint()
        mBarPaint?.style = Paint.Style.FILL
        mBarPaint?.color = Color.BLACK
        mBarPaint?.isAntiAlias = true
        mBarPaint?.textSize = DensityUtil.dp2px(context, 14f).toFloat()
        mBarPaint?.strokeWidth = DensityUtil.dp2px(context, 1f).toFloat()

        detector = GestureDetector(context, BarGesture())
        detector?.setIsLongpressEnabled(true)

        setOnTouchListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        offset = DensityUtil.dp2px(context, 32f).toFloat()
        mBarMaxWidth = mWidth
        mBarSingleWidth = mBarMaxWidth / mBarShowNum
        mBarMaxHeight = mHeight - 2 * offset
        mBarBlankSize = DensityUtil.dp2px(context, mBarSingleWidth / 4).toFloat()

        val fontMetrics = mBarPaint?.fontMetricsInt
        mHeightBlankSize = (offset - fontMetrics!!.let { it.bottom - it.top }) / 2
        mTextHeight = (offset + fontMetrics.let { it.bottom - it.top }) / 2

        if (shader == null) {
            shader = LinearGradient(0f, 0f, mBarBlankSize*2,
                mBarMaxHeight, Color.WHITE, mColor, Shader.TileMode.CLAMP)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var count = 0
        for (index in mData!!.indices) {
            if (!checkIsNeedDraw(index)) {
                val height = (mMaxData-mData!![index])/mMaxData*mBarMaxHeight
                mBarPaint?.shader = null
                drawBarValue(canvas, index, height)
                drawDescriptions(canvas, index)
                mBarPaint?.shader = shader
                drawBars(canvas, height)
                count++
            }
            if (index != mData!!.size - 1) {
                canvas?.translate(mBarSingleWidth, 0f)
            }
        }
    }

    private fun checkIsNeedDraw(i: Int): Boolean {
        if (mBarSingleWidth*(i+1) < scrollX)
            return true

        if (mBarSingleWidth*i > scrollX+mWidth)
            return true

        return false
    }

    private fun drawBarValue(canvas: Canvas?, index: Int,
                             height: Float) {
        val value = mData?.get(index).toString()
        mBarPaint?.getTextBounds(
            value,
            0,
            value.length,
            mTextBounds
        )
        val blankSize = (mBarSingleWidth - (mTextBounds.right - mTextBounds.left)) / 2
        mBarPaint?.let { canvas?.drawText(value, blankSize, mTextHeight + height, it) }
    }

    private fun drawDescriptions(canvas: Canvas?, index: Int) {
        val description = mDescription?.get(index)
        mBarPaint?.getTextBounds(
            description,
            0,
            description?.length!!,
            mTextBounds
        )
        val blankSize = (mBarSingleWidth - (mTextBounds.right - mTextBounds.left)) / 2
        mBarPaint?.let {
            canvas?.drawText(
                description!!,
                blankSize,
                mTextHeight + mBarMaxHeight + offset,
                it
            )
        }
    }

    private fun drawBars(canvas: Canvas?, height: Float) {
        mBarPaint?.let {
            canvas?.drawRoundRect(
                mBarBlankSize,
                offset+mBarMaxHeight-scale*(mBarMaxHeight-height),
                mBarSingleWidth - mBarBlankSize,
                mBarMaxHeight + offset,
                mBarCornerSize,
                mBarCornerSize,
                it
            )
        }
    }

    private inner class BarGesture : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?,
                              distanceX: Float, distanceY: Float): Boolean {
            if (mBarShowNum <= mMinBarScrollShowNum)
                return false

            val position = DensityUtil.dp2px(context, scrollX.toFloat())

            if (distanceX >= 0) {
                if (position <= mBarMaxWidth) {
                    scrollBy(distanceX.toInt(), 0)
                }
            } else {
                if (distanceX >= -1 * position) {
                    scrollBy(distanceX.toInt(), 0)
                }
            }

            return false
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return detector!!.onTouchEvent(event)
    }

    private fun setData(data: List<Float>?,
                        description: List<String>) {
        if (data != null && data.isNotEmpty()
            && description != null && description.isNotEmpty()) {
            mData = data
            mDescription = description
            animator?.start()
        }
    }

    fun setData(barBeans: List<BarBean>) {
        val data: MutableList<Float> = ArrayList()
        val description: MutableList<String> = ArrayList()
        for (i in barBeans.indices) {
            barBeans[i].value?.let {
                data.add(it)
                if (mMaxData < it) {
                    mMaxData = it
                }
            }
            barBeans[i].description?.let {
                description.add(it)
            }
        }
        if (mBarShowNum > barBeans.size)
            mBarShowNum = barBeans.size
        fixMaxData()
        setData(data, description)
    }

    private fun fixMaxData() {
        mMaxData = ceil(mMaxData/40) * 40
    }

    fun setColor(color: Int) {
        mColor = color
    }
}