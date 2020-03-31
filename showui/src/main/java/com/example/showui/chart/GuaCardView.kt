package com.example.showui.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.showui.R

class GuaCardView : View, View.OnTouchListener {
    /**
     * 原图
     */
    private var SBitmap: Bitmap? = null
    private var SBitmapSize: Int? = null

    /**
     * 目标图片
     */
    private var DBitmap: Bitmap? = null

    private var bitmap: Bitmap? = null

    private var mPaint: Paint? = null

    private var xfermode: PorterDuffXfermode? = null

    /**
     * 记录手指划过的路劲
     */
    private val mPath: Path = Path()
    private var mPathMeasure: PathMeasure = PathMeasure(mPath, false)
    private var mLength: Float = 0f

    private var startX: Float? = null
    private var startY: Float? = null

    private var bitCanvas: Canvas? = null


    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init()
        setOnTouchListener(this)
    }

    private fun init() {
        mPaint = Paint()
        mPaint?.style = Paint.Style.STROKE
        mPaint?.strokeWidth = 45f

        SBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.one)
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.two)
        DBitmap =
            SBitmap?.let {
                Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
            }
        SBitmapSize = SBitmap?.let { it.width * it.height * 2 / 5 }

        bitCanvas = DBitmap?.let { Canvas(it) }
        xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bitmap?.let { canvas?.drawBitmap(it, 0f, 0f, mPaint) }
        if (mLength < SBitmapSize!!) {
            val layerId =
                canvas?.saveLayer(null, null)
            mPaint?.let { bitCanvas?.drawPath(mPath, it) }
            SBitmap?.let { canvas?.drawBitmap(it, 0f, 0f, mPaint) }
            // 叠加模式
            mPaint?.xfermode = xfermode
            DBitmap?.let { canvas?.drawBitmap(it, 0f, 0f, mPaint) }
            // 重置xfermode模式
            mPaint?.xfermode = null
            layerId?.let { canvas.restoreToCount(it) }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                mPath.quadTo(startX!!, startY!!, event.x, event.y)
                startX = event.x
                startY = event.y
                Log.e("GuaCardView,Move", startX.toString() + ":" + startY)
            }
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                Log.e("GuaCardView,Down", startX.toString() + ":" + startY)
                mPath.moveTo(startX!!, startY!!)
            }
            MotionEvent.ACTION_UP -> {
                Log.e("GuaCardView,Up", startX.toString() + ":" + startY)
                mPathMeasure = PathMeasure(mPath, false)
                mLength += mPathMeasure.length * mPaint?.strokeWidth!!
                Log.e("GuaCardView,mLength", mLength.toString() + ":" + SBitmapSize)
            }
        }
        postInvalidate()
        requestLayout()
        return true
    }
}