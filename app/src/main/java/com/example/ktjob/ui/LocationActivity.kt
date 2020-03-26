package com.example.ktjob.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity;
import com.example.ktjob.R
import com.example.ktjob.adapter.AreaItemAdapter
import com.example.ktjob.adapter.CityItemAdapter
import com.example.ktjob.adapter.ProvinceItemAdapter
import com.example.ktjob.db.*
import com.example.ktjob.json.Area
import com.example.ktjob.model.WeatherModel
import kotlinx.coroutines.*

class LocationActivity : AppCompatActivity(), CoroutineScope by MainScope()  {
    private val tag : String? = LocationActivity::class.simpleName

    private lateinit var mShowList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate")
        setContentView(R.layout.content_location)
        mShowList = findViewById(R.id.location_list)
        mShowList.setOnItemClickListener { adapterView, view, position: Int, id: Long ->
            var location = mShowList.adapter.getItem(position)
            if (location is ProvinceItem) {
                GlobalScope.launch(Dispatchers.IO) {
                    Log.i(tag, "getCityList")
                    var citys: List<CityItem> = LocationDatabase.getInstance(this@LocationActivity)
                        .getCityDao().getProvinceCity(location.code as String)
                    withContext(Dispatchers.Main) {
                        var adapter = CityItemAdapter(citys, this@LocationActivity)
                        mShowList.adapter = adapter
                    }
                }
            } else if (location is CityItem) {
                GlobalScope.launch(Dispatchers.IO) {
                    Log.i(tag, "getAreaList")
                    var areas: List<AreaItem> = LocationDatabase.getInstance(this@LocationActivity)
                        .getAreaDao().getCityArea(location.cityCode as String)
                    withContext(Dispatchers.Main) {
                        var adapter = AreaItemAdapter(areas, this@LocationActivity)
                        mShowList.adapter = adapter
                    }
                }
            } else if (location is AreaItem) {
                var sharedPreference = getSharedPreferences(WeatherModel.mSPName, Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                WeatherModel.mAreaName = location.areaName as String
                WeatherModel.mAreaCode = location.areaCode as String
                Log.i(tag, "change area " + WeatherModel.mAreaName)
                editor.putString(WeatherModel.mSPLocationKey, WeatherModel.mAreaName)
                editor.putString(WeatherModel.mSPCodeKey, WeatherModel.mAreaCode)
                editor.commit()
                GlobalScope.launch(Dispatchers.IO) {
                    WeatherDatabase.getInstance(this@LocationActivity).getAreaDao().update(location)
                    WeatherModel.getInstance().setCurrentLocation(location)
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume")
        showProvinceList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun showProvinceList() {
        GlobalScope.launch(Dispatchers.IO) {
            Log.i(tag, "getProvinceList")
            var provinces: List<ProvinceItem> = LocationDatabase.getInstance(this@LocationActivity).getProvinceDao().getAll()

            withContext(Dispatchers.Main) {
                var adapter = ProvinceItemAdapter(provinces, this@LocationActivity)
                mShowList.adapter = adapter
            }
        }
    }
}
