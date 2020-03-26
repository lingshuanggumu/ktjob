package com.example.ktjob.model

import com.example.ktjob.db.AreaItem

class WeatherModel {
    private lateinit var mCurrentArea: AreaItem

    fun setCurrentLocation(area : AreaItem) {
        mCurrentArea = area
    }

    fun getCurrentLocation() : AreaItem {
        return mCurrentArea
    }

    companion object {
        val mSPName = "Weather"

        val mSPLocationKey = "Area"

        val mSPCodeKey = "Code"

        var mAreaName = "pudong"

        var mAreaCode = "310115"

        @Volatile
        private var instance : WeatherModel? = null

        fun getInstance() : WeatherModel{
            if (instance == null){
                synchronized(WeatherModel::class){
                    if (instance == null){
                        instance = WeatherModel()
                    }
                }
            }
            return instance!!
        }
    }
}