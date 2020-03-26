package com.example.ktjob.db

import androidx.room.*

@Dao
interface WeatherHistoryDao {
    @Insert
    fun insert(history: WeatherHistory)

    @Query("DELETE FROM history WHERE area_code = :code")
    fun delete(code: String)

    @Update
    fun update(history: WeatherHistory)

    @Query("SELECT * FROM history WHERE area_code = :code")
    fun get(code: String): WeatherHistory
}