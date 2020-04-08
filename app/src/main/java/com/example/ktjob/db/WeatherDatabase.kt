package com.example.ktjob.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AreaItem::class, WeatherHistory::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getAreaDao() : AreaDao

    abstract fun getWeatherHistoryDao() : WeatherHistoryDao

    companion object {
        @Volatile
        private var instance : WeatherDatabase? = null
        fun init(context: Context) : WeatherDatabase{
            if (instance == null){
                synchronized(WeatherDatabase::class){
                    if (instance == null){
                        instance = Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java,"weather")
                            .build()
                    }
                }
            }
            return instance!!
        }

        fun getInstance() : WeatherDatabase{
            return instance!!
        }
    }
}