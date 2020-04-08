package com.example.ktjob.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity;
import com.example.ktjob.R
import com.example.ktjob.db.*
import com.example.ktjob.json.Location
import kotlinx.coroutines.*
import java.io.InputStream


class SplashActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val tag : String? = SplashActivity::class.simpleName

    private lateinit var mImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiFlag:Int = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        window.decorView.systemUiVisibility = uiFlag
        setContentView(R.layout.content_splash)
        Log.i(tag, "onCreate")
        mImage = findViewById(R.id.splash)
    }

    override fun onResume() {
        super.onResume()
        Log.i(tag, "onResume")
        showSplash()
    }

    fun showSplash() {
        GlobalScope.launch(Dispatchers.IO) {
            Log.i(tag, "showSplash")
            var dbInfo = LocationDatabase.getInstance().getDbInfoDao()
            if (dbInfo.get().isEmpty()) {
                dbInfo.insert(DbInfo(true))
                updateLocationDb()
            } else {
                //Log.i(tag, "clear all db table")
                //LocationDatabase.getInstance(this@SplashActivity).getAreaDao().deleteAll()
                //LocationDatabase.getInstance(this@SplashActivity).getCityDao().deleteAll()
                //LocationDatabase.getInstance(this@SplashActivity).getProvinceDao().deleteAll()
                //LocationDatabase.getInstance(this@SplashActivity).getDbInfoDao().deleteAll()
            }

            exitSplash()
        }
    }

    suspend fun exitSplash() {
        Log.i(tag, "exitSplash")
        delay(2000)
        startActivity(Intent(this, WeatherActivity::class.java))
        finish()
    }

    private fun updateLocationDb() {
        //val resultType = object : TypeToken<Location>() {}.type
        val inputStream = resources.openRawResource(R.raw.city_code)
        val jsonData: String = readFileToString(inputStream)
        val location: Location = com.example.jobutils.GsonUtil.getGson().fromJson(jsonData, Location::class.java)
        Log.i(tag, "updateLocationDb")
        LocationDatabase.getInstance().getDbInfoDao().insert(DbInfo(true))
        location.province.forEach {
            //Log.i(tag, it.name)
            LocationDatabase.getInstance().getProvinceDao().insert(ProvinceItem(it))
            if (it.city != null) {
                val province = it
                it.city.forEach {
                    LocationDatabase.getInstance().getCityDao().insert(CityItem(province, it))
                    if (it.area != null) {
                        val city = it
                        it.area.forEach {
                            LocationDatabase.getInstance().getAreaDao().insert(AreaItem(province, city, it))
                        }
                    }
                }
            }
        }
    }

    private fun readFileToString(input: InputStream): String {
        Log.i(tag, "readFileToString")
        val sb: StringBuilder = StringBuilder()
        input.buffered().reader().use { reader ->
            sb.append(reader.readText())
        }
        return sb.toString()
    }
}
