package com.example.ktjob

import android.app.Application
import com.example.ktjob.db.LocationDatabase
import com.example.ktjob.db.WeatherDatabase

class JobApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //Initialize databases
        WeatherDatabase.init(this)
        LocationDatabase.init(this)
    }
}