package com.example.ktjob.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.facegate.ui.FaceActivity
import com.example.ktjob.R
import com.example.ktjob.db.LocationDatabase
import com.example.ktjob.model.WeatherModel
import com.example.ktjob.adapter.WeatherLocationAdapter
import com.example.ktjob.fragment.WeatherFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

class WeatherActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    TabLayout.OnTabSelectedListener  {
    private val tag : String? = WeatherActivity::class.simpleName

    private lateinit var mBtJetpack: Button

    private lateinit var mBtTranslation: Button

    private lateinit var mBtFace: Button

    private lateinit var mNavBar: BottomNavigationView

    private lateinit var mTabLayout: TabLayout

    private lateinit var mViewPager: ViewPager

    private lateinit var mToolbar: Toolbar

    //private lateinit var mBar: BarChartView

    override fun onTabReselected(tab: TabLayout.Tab?) {
        Log.i(tag, "onTabReselected: " + tab!!.position)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        Log.i(tag, "onTabSelected: " + tab!!.position)
        mViewPager.run {
            currentItem = tab!!.position
            var areaFragment = adapter!!.instantiateItem(this, currentItem) as WeatherFragment
            WeatherModel.getInstance().setCurrentLocation(areaFragment.getArea())
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        Log.i(tag, "onTabUnselected: " + tab!!.position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate")
        setContentView(R.layout.content_main)

        this.supportActionBar!!.setTitle(this.getString(R.string.weather_module))

        mBtJetpack = findViewById(R.id.jetpack_module)
        mBtFace = findViewById(R.id.face_module)
        mBtTranslation = findViewById(R.id.translation_module)
        mNavBar = findViewById(R.id.bottom_nav)
        mTabLayout = findViewById(R.id.weather_tab)
        mViewPager = findViewById(R.id.weather_viewpager)
        mTabLayout.addOnTabSelectedListener(this)

        /*mBtJetpack.setOnClickListener {
            startActivity(Intent(this, JetpackActivity::class.java))
            finish()
        }

        mBtTranslation.setOnClickListener {
            startActivity(Intent(this, TranslationActivity::class.java))
        }

        mBtFace.setOnClickListener {
            startActivity(Intent(this, FaceActivity::class.java))
        }*/

        mNavBar.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                var retValue = false
                when (item.itemId) {
                    R.id.nav_weather -> {
                        retValue = true
                    }
                    R.id.nav_face -> {
                        startActivity(Intent(this@WeatherActivity, FaceActivity::class.java))
                        retValue = true
                    }
                    R.id.nav_translation -> {
                        startActivity(Intent(this@WeatherActivity, TranslationActivity::class.java))
                        retValue = true
                    }
                }
                return retValue
            }
        })

        var sharedPreference = getSharedPreferences(WeatherModel.mSPName, Context.MODE_PRIVATE)
        var insert = false
        if (sharedPreference.contains(WeatherModel.mSPLocationKey)) {
            WeatherModel.run {
                mAreaName = sharedPreference.getString(mSPLocationKey, mAreaName)
                mAreaCode = sharedPreference.getString(mSPCodeKey, mAreaCode)
                Log.i(tag, "onCreate ".plus(mAreaName))
            }

        } else {
            var editor = sharedPreference.edit()
            WeatherModel.run {
                editor.putString(mSPLocationKey, mAreaName)
                editor.putString(mSPCodeKey, mAreaCode)
                insert = true
            }
            editor.commit()
        }

        GlobalScope.launch(Dispatchers.IO) {
            var area = LocationDatabase.getInstance().getAreaDao().getArea(WeatherModel.mAreaCode)
            Log.i(tag, "initTab" + area.areaName.toString())
            area?.let {
                WeatherModel.getInstance().setCurrentLocation(it)
                if (insert) {
                    WeatherModel.getInstance().addFavorArea(it)
                }
            }
        }

        //initBarDatas()
    }

    private fun initTab() {
        Log.i(tag, "initTab")
        var weatherTabList = mutableListOf<Fragment>()
        GlobalScope.launch(Dispatchers.IO) {
            var locations = WeatherModel.getInstance().getAllFavorArea()
            locations.forEach {
                weatherTabList.add(WeatherFragment(it))
                Log.i(tag, it.areaName+" "+it.areaCode)
            }
            withContext(Dispatchers.Main) {
                mViewPager.adapter = WeatherLocationAdapter(weatherTabList.toList(), supportFragmentManager)
                mTabLayout.setupWithViewPager(mViewPager)
                locations.forEachIndexed { index, value ->
                    mTabLayout.getTabAt(index)?.text = value.areaName
                }
            }
        }
    }

    private fun removeFromTab() {
        Log.i(tag, "removeFromTab")
        if (mViewPager.childCount <= 1)
            return

        var weatherTabList = mutableListOf<Fragment>()
        var sharedPreference = getSharedPreferences(WeatherModel.mSPName, Context.MODE_PRIVATE)

        GlobalScope.launch(Dispatchers.IO) {
            var area = WeatherModel.getInstance().getCurrentLocation()
            Log.i(tag, "removeFromTab" + area.areaName)
            WeatherModel.getInstance().removeFavorArea(area)

            var locations = WeatherModel.getInstance().getAllFavorArea()
            locations.forEach {
                weatherTabList.add(WeatherFragment(it))
                Log.i(tag, it.areaName+" "+it.areaCode)
            }
            withContext(Dispatchers.Main) {
                mViewPager.adapter = WeatherLocationAdapter(weatherTabList.toList(), supportFragmentManager)
                mTabLayout.setupWithViewPager(mViewPager)
                locations.forEachIndexed { index, value ->
                    mTabLayout.getTabAt(index)?.text = value.areaName
                }
                var editor = sharedPreference.edit()
                WeatherModel.run {
                    getInstance().setCurrentLocation(locations.get(0))
                    editor.putString(mSPLocationKey, mAreaName)
                    editor.putString(mSPCodeKey, mAreaCode)
                }
                editor.commit()
            }
        }
    }

    override fun onResume() {
        Log.i(tag, "onResume")
        initTab()
        super.onResume()
        mNavBar.selectedItemId = R.id.nav_weather
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this@WeatherActivity, LocationActivity::class.java))
            }
            R.id.action_remove -> {
                removeFromTab()
            }
            R.id.action_refresh -> {

            }
        }
        return true
    }

    /*private fun initBarDatas() {
        val barBean0 = BarBean(30f, "描述一")
        val barBean1 = BarBean(25f, "描述二")
        val barBean2 = BarBean(10f, "描述三")
        val barBean3 = BarBean(15f, "描述四")
        val barBean4 = BarBean(20f, "描述五")
        val barBeans: MutableList<BarBean> = ArrayList()
        barBeans.add(barBean0)
        barBeans.add(barBean1)
        barBeans.add(barBean2)
        barBeans.add(barBean3)
        barBeans.add(barBean4)

        weather_bar.setData(barBeans)
    }*/
}
