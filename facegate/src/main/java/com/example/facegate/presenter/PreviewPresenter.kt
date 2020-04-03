package com.example.facegate.presenter

import android.graphics.*
import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.facegate.json.FaceDetectSuccess
import com.example.facegate.model.FaceModel
import com.example.facegate.net.FaceApis
import com.example.facegate.view.FacePreviewCallback
import com.example.jobutils.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PreviewPresenter : FacePreviewCallback, LifecycleObserver,
    Camera.PreviewCallback, SurfaceHolder.Callback {
    private var tag = PreviewPresenter::class.simpleName

    private lateinit var mSurfaceHolder: SurfaceHolder

    private lateinit var mCamera: Camera

    private var mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK

    private val mFaceRecorder = MediaRecorder()

    private val mFaceApi : FaceApis = RetrofitHelper.createApi(FaceApis::class.java, FaceModel.mFaceUrl)

    private var mDefaultWidth = 1920

    private var mDefaultHeight = 1080

    private val mRestrict = 5

    private var previewCount = 0

    private var mSurfaceReady = false

    private var mCameraOpen = false

    override fun onRecordViewTouched(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> startRecord()
            MotionEvent.ACTION_UP -> stopRecord()
            else -> Log.i(tag, "onTouch default")
        }
        return true
    }

    override fun onPlayViewClicked(v: View?) {

    }

    override fun onCameraSwitchClicked(v: View?) {

    }


    fun disconnectListener() {
        releaseCamera()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.i(tag, "surfaceCreated")
        openCamera(mCameraId)
        mSurfaceReady = true
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.i(tag, "surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.i(tag, "surfaceDestroyed")
        releaseCamera()
        mSurfaceReady = false
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        //Log.i(tag, "onPreviewFrame")
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            if (previewCount >= mRestrict) {
                previewCount = 0
                //detectFaces(data)
            } else {
                previewCount++
            }
        }
    }

    fun initPresenter(holder: SurfaceHolder?, width: Int, height: Int) {
        mSurfaceHolder = holder as SurfaceHolder
        mSurfaceHolder.addCallback(this)
        mDefaultWidth = width
        mDefaultHeight = height
    }

    private fun openCamera(id: Int) {
        if (!mCameraOpen) {
            Log.i(tag, "openCamera")
            mCamera = Camera.open(id)
            mCamera.parameters.run {
                setPreviewSize(mDefaultWidth, mDefaultHeight)
                setPreviewFpsRange(15, 30)
            }

            mCamera.setPreviewDisplay(mSurfaceHolder)
            mCamera.setPreviewCallback(this)
            mCamera.startPreview()
            mCamera.setDisplayOrientation(90)
            mCameraOpen = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun releaseCamera() {
        if (mCameraOpen) {
            Log.i(tag, "releaseCamera")
            mCamera.run {
                stopPreview()
                setPreviewCallback(null)
                release()
                mCameraOpen = false
            }
        }
    }

    private fun startRecord() {
        Log.i(tag, "startRecord")
        if (mSurfaceReady) {
            mCamera.unlock()

            mFaceRecorder.setCamera(mCamera)

            mFaceRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
            mFaceRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA)
            mFaceRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P))

            var dir = File(Environment.getExternalStorageDirectory(), "job")
            if (!dir.exists()) {
                dir.mkdir()
            }

            var file = File(dir, SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date()).plus(".mp4"))
            mFaceRecorder.setOutputFile(file.path)
            mFaceRecorder.setPreviewDisplay(mSurfaceHolder.surface)
            mFaceRecorder.prepare()
            mFaceRecorder.start()
        }
    }

    private fun stopRecord() {
        Log.i(tag, "stopRecord")
        if (mSurfaceReady) {
            mFaceRecorder.stop()
            mFaceRecorder.reset()
            mFaceRecorder.release()
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

                            /*var bitmap = Bitmap.createBitmap(mDrawView.width, mDrawView.height, Bitmap.Config.RGB_565)
                            var canvas = Canvas(bitmap)
                            //var rect = Rect(leftValue, topValue, rightValue, bottomValue)
                            var rect = Rect(topValue, leftValue, bottomValue, rightValue)
                            Log.i(tag, "Rect: " + rect.toString())

                            canvas.drawRect(rect, mPaint)
                            mDrawView.alpha = 0.1f
                            mDrawView.bringToFront()
                            mDrawView.setImageBitmap(bitmap)*/
                        }
                    }
                }

                override fun onFailure(call: Call<FaceDetectSuccess>?, t: Throwable?) {
                }
            })
        bitmap.recycle()
        baos.close()
    }

    private inline fun bytes2Bitmap(b: ByteArray): Bitmap {
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
}