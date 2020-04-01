package com.example.ktjob.jetpack

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class JetPackObserver : LifecycleObserver {
    private val tag = JetPackObserver::class.simpleName

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
        Log.i(tag, "ON RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disConnectListener() {
        Log.i(tag, "ON PAUSE")
    }
}