package com.example.ktjob.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.facegate.ui.FaceActivity
import com.example.ktjob.R
import com.example.ktjob.json.TranslationResult
import com.example.jobutils.RetrofitHelper
import com.example.ktjob.model.TranslationModel
import com.example.ktjob.net.TranslationApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TranslationActivity : AppCompatActivity() {
    val tag = TranslationActivity::class.simpleName

    private lateinit var mSrcView: EditText

    private lateinit var mDstView: TextView

    private lateinit var mBtTrans: Button

    private lateinit var mNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translation)

        this.supportActionBar!!.setTitle(this.getString(R.string.translation_module))

        mSrcView = findViewById(R.id.translation_src)
        mDstView = findViewById(R.id.translation_dst)
        mBtTrans = findViewById(R.id.btTranslate)
        mNavBar = findViewById(R.id.bottom_nav)

        mBtTrans.setOnClickListener {
            mSrcView.text?.let {
                val im: InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.hideSoftInputFromWindow(mBtTrans.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                TranslationModel.mTranslationApi.requestTranslation("zh", "en", TranslationModel.mTranslationAppID, TranslationModel.mTranslationKey, it.toString())
                    .enqueue(object: Callback<TranslationResult> {
                         override fun onResponse(call: Call<TranslationResult>?, response: Response<TranslationResult>?) {
                             val result = response!!.body()
                             result.translation.get(0).run {
                                 translated.forEach {
                                     mDstView.text = it.text
                                 }
                             }
                         }

                         override fun onFailure(call: Call<TranslationResult>?, t: Throwable?) {
                             Log.i(tag, "onFailure")
                         }
                     })
            }
        }


        mNavBar.setOnNavigationItemSelectedListener(object: BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                var retValue = false
                when (item.itemId) {
                    R.id.nav_weather -> {
                        startActivity(Intent(this@TranslationActivity, WeatherActivity::class.java))
                        retValue = true
                    }
                    R.id.nav_face -> {
                        startActivity(Intent(this@TranslationActivity, FaceActivity::class.java))
                        retValue = true
                    }
                    R.id.nav_translation -> {
                        retValue = true
                    }
                }
                return retValue
            }
        })
    }


    override fun onResume() {
        super.onResume()
        mNavBar.selectedItemId = R.id.nav_translation
    }
}
