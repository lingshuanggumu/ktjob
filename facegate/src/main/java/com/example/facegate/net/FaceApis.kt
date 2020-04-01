package com.example.facegate.net

import com.example.facegate.json.FaceDetectSuccess
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FaceApis {
    @FormUrlEncoded
    @POST("facepp/v3/detect")
    fun detectFace(@Field("api_key")key: String, @Field("api_secret")secret: String, @Field("image_base64")data: String): Call<FaceDetectSuccess>
}