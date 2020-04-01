package com.example.facegate.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.facegate.R
import com.example.facegate.json.FaceDetectSuccess
import com.example.facegate.model.FaceModel
import com.example.facegate.net.FaceApis
import com.example.jobutils.RetrofitHelper
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class FaceActivity : AppCompatActivity(), Camera.PreviewCallback, SurfaceHolder.Callback {

    private val tag = FaceActivity::class.simpleName

    private val mFaceApi : FaceApis = RetrofitHelper.createApi(FaceApis::class.java, FaceModel.mFaceUrl)

    private lateinit var mPreview: SurfaceView

    private lateinit var mDrawView: ImageView

    private lateinit var mCamera: Camera

    private var mDisplayMetrics = DisplayMetrics()

    private var mDefaultWidth = 1920

    private var mDefaultHeight = 1080

    private val mRestrict = 5

    private var previewCount = 0

    private var mPaint = Paint()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPreview = findViewById(R.id.face_preview)
        mPreview.holder.addCallback(this)

        mDrawView = findViewById(R.id.face_rect)

        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10f

        getScreenInfo()

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 0x01)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }

    private fun getScreenInfo() {
        windowManager.defaultDisplay.getMetrics(mDisplayMetrics)
        mDefaultWidth = (mDisplayMetrics.widthPixels/mDisplayMetrics.density).toInt()
        mDefaultHeight = (mDisplayMetrics.heightPixels/mDisplayMetrics.density).toInt()
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        //Log.i(tag, "onPreviewFrame")
        if (previewCount >= mRestrict) {
            previewCount = 0
            detectFaces(data)
        } else {
            previewCount++
        }
    }

    private fun detectFaces(data: ByteArray?) {
        //Log.i(tag, "detectFaces")
        var yuvImage = YuvImage(
            data,
            ImageFormat.NV21,
            mCamera.parameters.previewSize.width,
            mCamera.parameters.previewSize.height,
            null
        )
        var baos = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            Rect(
                0,
                0,
                mCamera.parameters.previewSize.width,
                mCamera.parameters.previewSize.height
            ), 100, baos
        )

        var bitmap = bytes2Bitmap(baos.toByteArray())
        //saveImage(bitmap)
        var baso2 = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baso2)


        mFaceApi.detectFace(FaceModel.mAPIKey, FaceModel.mAPISecret,
            Base64.encodeToString(baso2.toByteArray(), Base64.DEFAULT))
            .enqueue(object : Callback<FaceDetectSuccess> {
                override fun onResponse(call: Call<FaceDetectSuccess>?, response: Response<FaceDetectSuccess>?) {
                    response?.let {
                        Log.i(tag, "code: "+response.code().toString())
                        if (it.code() == 200 &&  it.body().face_num > 0) {
                            Log.i(tag, it.body().toString())
                            var leftValue = it.body().faces.get(0).face_rectangle.left
                            var rightValue = leftValue + it.body().faces.get(0).face_rectangle.width
                            var topValue = it.body().faces.get(0).face_rectangle.top
                            var bottomValue = topValue + it.body().faces.get(0).face_rectangle.height

                            var bitmap = Bitmap.createBitmap(mDrawView.width, mDrawView.height, Bitmap.Config.RGB_565)
                            var canvas = Canvas(bitmap)
                            //var rect = Rect(leftValue, topValue, rightValue, bottomValue)
                            var rect = Rect(topValue, leftValue, bottomValue, rightValue)
                            Log.i(tag, "Rect: " + rect.toString())

                            canvas.drawRect(rect, mPaint)
                            mDrawView.alpha = 0.1f
                            mDrawView.bringToFront()
                            mDrawView.setImageBitmap(bitmap)
                        }
                    }
                }

                override fun onFailure(call: Call<FaceDetectSuccess>?, t: Throwable?) {
                }
            })
        bitmap.recycle()
        baos.close()
    }

    private inline fun bytes2Bitmap(b: ByteArray):Bitmap {
        return BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    private fun saveImage(bmp: Bitmap): File {
        var dir = File(Environment.getExternalStorageDirectory(), "job")
        if (!dir.exists()) {
            dir.mkdir()
        }

        var fileName = System.currentTimeMillis().toString().plus(".jpg")
        var file = File(dir, fileName)
        var fos = FileOutputStream(file)
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } finally {
            fos.flush()
            fos.close()
        }
        return file
    }


    override fun surfaceCreated(holder: SurfaceHolder?) {
        openCamera()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.i(tag, "surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    private fun openCamera() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
        mCamera.parameters.run {
            setPreviewSize(mDefaultWidth, mDefaultHeight)
            setPreviewFpsRange(15, 30)
        }

        mCamera.setPreviewDisplay(mPreview.holder)
        mCamera.setPreviewCallback(this)
        mCamera.startPreview()
        mCamera.setDisplayOrientation(90)
    }

    private fun releaseCamera() {
        mCamera.run {
            stopPreview()
            setPreviewCallback(null)
        }
    }
}
