package com.example.ktjob.net

import com.example.ktjob.json.TranslationResult
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TranslationApi {
    @FormUrlEncoded
    @POST("dotranslate.php")
    fun requestTranslation(@Field("from")from: String, @Field("to")to: String,
                           @Field("app_kid")app_kid: String, @Field("app_key")app_key: String,
                           @Field("text")text: String): Call<TranslationResult>
}