package com.example.ktjob.model

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.lifecycle.ViewModel
import com.example.jobutils.RetrofitHelper
import com.example.ktjob.db.AreaItem
import com.example.ktjob.db.WeatherDatabase
import com.example.ktjob.net.WeatherApi

class WeatherModel : ViewModel() {
    private lateinit var mCurrentArea: AreaItem

    private var mFavorList = mutableListOf<AreaItem>()

    fun setCurrentLocation(area : AreaItem) {
        mCurrentArea = area
        mAreaName = area.areaName
        mAreaCode = area.areaCode
    }

    fun getCurrentLocation() : AreaItem {
        return mCurrentArea
    }

    fun addFavorArea(area: AreaItem) {
        WeatherDatabase.getInstance().getAreaDao().insert(area)
        mFavorList.add(area)
        mCurrentArea = area
    }

    fun removeFavorArea(area: AreaItem) {
        WeatherDatabase.getInstance().getAreaDao().delete(area.areaCode)
        mFavorList.remove(area)
    }

    fun getAllFavorArea(): List<AreaItem> {
        return WeatherDatabase.getInstance().getAreaDao().getAllArea()
    }

    companion object {
        const val mWeatherUrl = "https://free-api.heweather.net/s6/weather/"

        const val mWeatherUserKey = "ff86cd581608458991f4745593274876"

        val mSPName = "Weather"

        val mSPLocationKey = "Area"

        val mSPCodeKey = "Code"

        var mAreaName = "pudong"

        var mAreaCode = "310115"

        val mWeatherApi: WeatherApi = RetrofitHelper.createApi(WeatherApi::class.java, mWeatherUrl)

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

        fun getDrawableResId(context: Context, imageName: String): Int {
            var appInfo: ApplicationInfo = context.applicationInfo
            val name = "cond_"+imageName
            //Log.i(tag, "pic name = " + name)
            return context.resources.getIdentifier(name, "drawable", appInfo.packageName)
        }
    }
}