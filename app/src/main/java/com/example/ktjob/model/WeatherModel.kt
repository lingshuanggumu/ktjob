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
        const val mWeatherUrl = "https://free-api.heweather.net/s6/weather/"

        const val mWeatherUserKey = "ff86cd581608458991f4745593274876"

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