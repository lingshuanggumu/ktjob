package com.example.ktjob.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.marginLeft
import com.bumptech.glide.Glide
import com.example.ktjob.R
import com.example.ktjob.db.LocationDatabase
import com.example.ktjob.db.WeatherDatabase
import com.example.ktjob.db.WeatherHistory
import com.example.ktjob.json.WeatherResult
import com.example.ktjob.model.WeatherModel
import com.example.ktjob.net.RetrofitHelper
import com.example.ktjob.net.WeatherApi
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope()  {
    private val tag : String? = MainActivity::class.simpleName

    private val mPadding = 20

    private val mWeatherApi: WeatherApi = RetrofitHelper.createApi(WeatherApi::class.java, RetrofitHelper.mWeatherUrl)

    private var mScreenWidth: Int = 0

    private var mScreenHeight: Int = 0

    private var mDisplayMetrics = DisplayMetrics()

    private lateinit var mLocation: TextView

    private lateinit var mWeatherInfo: ViewGroup

    private lateinit var mWeatherCondition: TextView

    private lateinit var mWeatherTemperature: TextView

    private lateinit var mWeatherImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate")
        setContentView(R.layout.content_main)

        mLocation = findViewById(R.id.weather_location)
        mWeatherInfo = findViewById(R.id.weather_info)
        mWeatherCondition = findViewById(R.id.weather_condtion)
        mWeatherTemperature = findViewById(R.id.weather_temperature)
        mWeatherImage = findViewById(R.id.weather_condtion_img)

        //Glide.with(this).load(R.drawable.cond_999).into(mWeatherImage)

        mLocation.setOnClickListener { view ->
            startActivity(Intent(this, LocationActivity::class.java))
            //finish()
        }

        var sharedPreference = getSharedPreferences(WeatherModel.mSPName, Context.MODE_PRIVATE)
        if (sharedPreference.contains(WeatherModel.mSPLocationKey)) {
            //WeatherModel.mAreaName = sharedPreference.getString(WeatherModel.mSPLocationKey, WeatherModel.mAreaName)
            WeatherModel.mAreaName = sharedPreference.getString(WeatherModel.mSPLocationKey, WeatherModel.mAreaName)
            WeatherModel.mAreaCode = sharedPreference.getString(WeatherModel.mSPCodeKey, WeatherModel.mAreaCode)
            Log.i(tag, "onCreate " + WeatherModel.mAreaName)
        } else {
            var editor = sharedPreference.edit()
            editor.putString(WeatherModel.mSPLocationKey, WeatherModel.mAreaName)
            editor.putString(WeatherModel.mSPCodeKey, WeatherModel.mAreaCode)
            editor.commit()
        }
        GlobalScope.launch(Dispatchers.IO) {
            var area = LocationDatabase.getInstance(this@MainActivity).getAreaDao().getArea(WeatherModel.mAreaCode)
            WeatherModel.getInstance().setCurrentLocation(area)
        }
    }

    private fun getScreenInfo() {
        windowManager.defaultDisplay.getMetrics(mDisplayMetrics)
        mScreenWidth = (mDisplayMetrics.widthPixels/mDisplayMetrics.density).toInt()
        mScreenHeight = (mDisplayMetrics.heightPixels/mDisplayMetrics.density).toInt()
    }

    private fun getDrawableResId(context: Context, imageName: String): Int {
        var appInfo: ApplicationInfo = context.applicationInfo
        val name = "cond_"+imageName
        Log.i(tag, "pic name = " + name)
        return context.resources.getIdentifier(name, "drawable", appInfo.packageName)
    }

    override fun onResume() {
        super.onResume()
        getScreenInfo()

        mWeatherCondition.width = (mScreenWidth/2 - mPadding) * mDisplayMetrics.density.toInt()
        mWeatherTemperature.width = (mScreenWidth/2 - mPadding) * mDisplayMetrics.density.toInt()

        Log.i(tag, "onResume, area " + WeatherModel.mAreaName)
        var queryMap = mapOf("location" to WeatherModel.mAreaName)
        mWeatherApi.requestForecast("now", RetrofitHelper.mWeatherUserKey, queryMap)
            .enqueue(object: Callback<WeatherResult> {
                override fun onResponse(call: Call<WeatherResult>?, response: Response<WeatherResult>?) {
                    Log.i(tag, "onResponse: " + response.toString())
                    var result: WeatherResult? = response!!.body()
                    //Log.i(tag, result!!.HeWeather6.get(0).now.cond_txt)
                    GlobalScope.launch(Dispatchers.IO) {
                        if (result!!.HeWeather6.get(0).basic != null) {
                            withContext(Dispatchers.Main) {
                                mLocation.text = result!!.HeWeather6.get(0).basic.location
                                mWeatherTemperature.text = result!!.HeWeather6.get(0).now.tmp
                                mWeatherCondition.text = result!!.HeWeather6.get(0).now.cond_txt
                            }

                            val code = WeatherModel.getInstance().getCurrentLocation().areaCode as String
                            WeatherDatabase.getInstance(this@MainActivity).getWeatherHistoryDao().update(
                                WeatherHistory(code, result!!.HeWeather6.get(0).basic.location,
                                    result!!.HeWeather6.get(0).now.tmp, result!!.HeWeather6.get(0).now.cond_txt,
                                    result!!.HeWeather6.get(0).now.cond_code,
                                    result!!.HeWeather6.get(0).update.loc)
                            )
                            Log.i(tag, result!!.HeWeather6.get(0).basic.location + " "
                                        + result!!.HeWeather6.get(0).update.loc);

                            val name = result!!.HeWeather6.get(0).now.cond_code
                            withContext(Dispatchers.Main) {
                                Log.i(tag, "condition code = " + name)
                                Glide.with(this@MainActivity).load(getDrawableResId(this@MainActivity, name)).into(mWeatherImage)
                                var margin = (mScreenWidth - mWeatherImage.drawable.bounds.width())  * mDisplayMetrics.density.toInt()
                                Log.i(tag, "onResume padding = " + (margin/2) + ", width = " + mWeatherImage.drawable.bounds.width())
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResult>?, t: Throwable?) {
                    Log.i(tag, "onFailure")
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
