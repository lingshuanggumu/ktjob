package com.example.ktjob.db

import androidx.room.*

@Dao
interface AreaDao {
    @Insert
    fun insert(area: AreaItem)

    @Update
    fun update(area: AreaItem)

    @Query("DELETE FROM area WHERE area_code = :code")
    fun delete(code: String)

    @Query("SELECT * FROM area WHERE city_code = :code")
    fun getCityArea(code: String): List<AreaItem>

    @Query("SELECT * FROM area WHERE area_code = :code")
    fun getArea(code: String): AreaItem

    @Query("DELETE FROM area")
    fun deleteAll()
}