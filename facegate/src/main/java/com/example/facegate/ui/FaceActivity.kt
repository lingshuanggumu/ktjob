package com.example.facegate.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.example.facegate.R
import com.example.facegate.presenter.PreviewPresenter

class FaceActivity : AppCompatActivity() {

    private val tag = FaceActivity::class.simpleName

    private lateinit var mPreview: SurfaceView

    private lateinit var mRecordView: ImageView

    private lateinit var mPlayView: ImageView

    private lateinit var mSwitchCamera: ImageView

    private var mDisplayMetrics = DisplayMetrics()

    private val mRequestCode = 0x100

    private var mPresenter = PreviewPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
    }

    private fun requestPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), mRequestCode)
        } else {
            setupView(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == mRequestCode && grantResults != null && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupView(this);
            }
        }
    }

    private fun setupView(context: Context) {
        setContentView(R.layout.activity_main)
        
        mPreview = findViewById(R.id.face_preview)

        mRecordView = findViewById(R.id.record_button)

        mPlayView = findViewById(R.id.play_button)

        mSwitchCamera = findViewById(R.id.switch_camera)

        mRecordView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return mPresenter.onRecordViewTouched(v, event)
            }
        })

        mPlayView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                mPresenter.onPlayViewClicked(v)
            }
        })

        mSwitchCamera.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                mPresenter.onCameraSwitchClicked(v)
            }
        })

        lifecycle.addObserver(mPresenter)

        windowManager.defaultDisplay.getMetrics(mDisplayMetrics)
        var width = (mDisplayMetrics.widthPixels/mDisplayMetrics.density).toInt()
        var height = (mDisplayMetrics.heightPixels/mDisplayMetrics.density).toInt()
        mPresenter.initPresenter(mPreview.holder, width, height)
    }

    override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume")
    }
}
