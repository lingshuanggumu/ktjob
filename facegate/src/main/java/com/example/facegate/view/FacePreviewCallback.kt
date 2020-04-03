package com.example.facegate.view

import android.view.MotionEvent
import android.view.View

interface FacePreviewCallback {
    fun onRecordViewTouched(v: View?, event: MotionEvent?): Boolean

    fun onPlayViewClicked(v: View?)

    fun onCameraSwitchClicked(v: View?)
}