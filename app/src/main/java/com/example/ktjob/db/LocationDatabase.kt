package com.example.ktjob.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProvinceItem::class, CityItem::class, AreaItem::class, DbInfo::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun getProvinceDao() : ProvinceDao

    abstract fun getCityDao() : CityDao

    abstract fun getAreaDao() : AreaDao

    abstract fun getDbInfoDao() : DbInfoDao

    companion object {
        @Volatile
        private var instance : LocationDatabase? = null
        fun getInstance(context: Context) : LocationDatabase{
            if (instance == null){
                synchronized(LocationDatabase::class){
                    if (instance == null){
                        instance = Room.databaseBuilder(context.applicationContext, LocationDatabase::class.java,"location")
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
}