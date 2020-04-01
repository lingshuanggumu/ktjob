package com.example.jobutils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
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