package com.example.ktjob.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CityDao {
    @Insert
    fun insert(city: CityItem)

    @Delete
    fun delete(city: CityItem)

    @Query("SELECT * FROM city WHERE province_code = :code")
    fun getProvinceCity(code: String): List<CityItem>

    @Query("DELETE FROM city")
    fun deleteAll()
}