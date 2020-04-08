package com.example.ktjob.model

import com.example.jobutils.RetrofitHelper
import com.example.ktjob.net.TranslationApi

class TranslationModel {
    companion object {
        const val mTranslationUrl = "http://api.yeekit.com/"

        const val mTranslationAppID = "5e82ba7bc0f7d"

        const val mTranslationKey = "cb93101609b19a8bc463a5df70347a08"

        val mTranslationApi: TranslationApi = RetrofitHelper.createApi(TranslationApi::class.java, mTranslationUrl)
    }
}