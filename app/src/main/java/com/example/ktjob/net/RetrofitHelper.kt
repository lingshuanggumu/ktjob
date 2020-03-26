package com.example.ktjob.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        const val mWeatherUrl = "https://free-api.heweather.net/s6/weather/"

        const val mWeatherUserKey = "ff86cd581608458991f4745593274876"

        private val mOkHttpClient: OkHttpClient = OkHttpClient.Builder().build()

        fun <T> createApi(clazz: Class<T>, url: String): T {
            var retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(clazz)
        }
    }
}