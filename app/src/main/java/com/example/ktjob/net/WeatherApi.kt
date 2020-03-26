package com.example.ktjob.net

import com.example.ktjob.json.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface WeatherApi {
    @GET("{weatherType}")
    fun requestForecast(@Path("weatherType")type: String, @Query("key") key: String, @QueryMap params: Map<String, String>): Call<WeatherResult>
}