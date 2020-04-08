package com.example.ktjob.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.example.ktjob.R
import com.example.ktjob.db.AreaItem
import com.example.ktjob.db.WeatherDatabase
import com.example.ktjob.db.WeatherHistory
import com.example.ktjob.json.WeatherResult
import com.example.ktjob.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherFragment(areaItem: AreaItem) : Fragment() {

    private val mTag = WeatherFragment::class.simpleName

    private lateinit var viewModel: WeatherModel

    private lateinit var mWeatherCondition: TextView

    private lateinit var mWeatherTemperature: TextView

    private lateinit var mWeatherImage: ImageView

    private var mArea = areaItem

    fun getArea() : AreaItem {
        return mArea
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(mTag, "onCreateView")
        var view = inflater.inflate(R.layout.weather_fragment, container, false)
        mWeatherCondition = view.findViewById(R.id.weather_condtion)
        mWeatherTemperature = view.findViewById(R.id.weather_temperature)
        mWeatherImage = view.findViewById(R.id.weather_condtion_img)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(mTag, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeatherModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        Log.i(mTag, "onResume, area " + mArea.areaName)
        var queryMap = mapOf("location" to mArea.areaName)
        WeatherModel.mWeatherApi.requestForecast("now", WeatherModel.mWeatherUserKey, queryMap)
            .enqueue(object: Callback<WeatherResult> {
                override fun onResponse(call: Call<WeatherResult>?, response: Response<WeatherResult>?) {
                    Log.i(mTag, "onResponse: " + response.toString())
                    var result: WeatherResult? = response!!.body()
                    //Log.i(tag, result!!.HeWeather6.get(0).now.cond_txt)
                    GlobalScope.launch(Dispatchers.IO) {
                        if (result!!.HeWeather6.get(0).basic != null) {
                            withContext(Dispatchers.Main) {
                                if (isAdded()) {
                                    mWeatherTemperature.text =
                                        result!!.HeWeather6.get(0).now.tmp.plus(" ").plus(getString(R.string.celsius))
                                    mWeatherCondition.text = result!!.HeWeather6.get(0).now.cond_txt
                                }
                            }

                            //val code = WeatherModel.getInstance().getCurrentLocation().areaCode as String
                            WeatherDatabase.getInstance().getWeatherHistoryDao().update(
                                WeatherHistory(mArea.areaCode, result!!.HeWeather6.get(0).basic.location,
                                    result!!.HeWeather6.get(0).now.tmp, result!!.HeWeather6.get(0).now.cond_txt,
                                    result!!.HeWeather6.get(0).now.cond_code,
                                    result!!.HeWeather6.get(0).update.loc)
                            )
                            Log.i(tag, result!!.HeWeather6.get(0).basic.location + " "
                                    + result!!.HeWeather6.get(0).update.loc);

                            val name = result!!.HeWeather6.get(0).now.cond_code
                            withContext(Dispatchers.Main) {
                                Log.i(mTag, "condition code = " + name)
                                if (isAdded()) {
                                    Glide.with(this@WeatherFragment.context).load(
                                        WeatherModel.getDrawableResId(
                                            this@WeatherFragment.context as Context,
                                            name
                                        )
                                    ).into(mWeatherImage)
                                }
                                /*var margin = (mScreenWidth - mWeatherImage.drawable.bounds.width())  * mDisplayMetrics.density.toInt()
                                Log.i(tag, "onResume padding = " + (margin/2) + ", width = " + mWeatherImage.drawable.bounds.width())*/
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResult>?, t: Throwable?) {
                    Log.i(mTag, "onFailure")
                }
            })
    }
}
