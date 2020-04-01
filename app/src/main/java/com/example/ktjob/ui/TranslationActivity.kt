package com.example.ktjob.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ktjob.R
import com.example.ktjob.json.TranslationResult
import com.example.jobutils.RetrofitHelper
import com.example.ktjob.model.TranslationModel
import com.example.ktjob.net.TranslationApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TranslationActivity : AppCompatActivity() {
    val tag = TranslationActivity::class.simpleName

    private val mTranslationApi: TranslationApi = RetrofitHelper.createApi(TranslationApi::class.java, TranslationModel.mTranslationUrl)

    private lateinit var mSrcView: EditText

    private lateinit var mDstView: TextView

    private lateinit var mBtTrans: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translation)
        mSrcView = findViewById(R.id.translation_src)
        mDstView = findViewById(R.id.translation_dst)
        mBtTrans = findViewById(R.id.btTranslate)

        mBtTrans.setOnClickListener {
            mSrcView.text?.let {
                val im: InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.hideSoftInputFromWindow(mBtTrans.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                mTranslationApi.requestTranslation("zh", "en", TranslationModel.mTranslationAppID, TranslationModel.mTranslationKey, it.toString())
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
    }
}
