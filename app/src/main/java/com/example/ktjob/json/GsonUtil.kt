package com.example.ktjob.json

import com.google.gson.Gson

class GsonUtil {
    companion object {
        @Volatile
        private var instance : Gson? = null
        fun getGson() : Gson {
            if (instance == null) {
                synchronized(GsonUtil::class) {
                    if (instance == null) {
                        instance = Gson()
                    }
                }
            }
            return instance!!
        }
    }
}