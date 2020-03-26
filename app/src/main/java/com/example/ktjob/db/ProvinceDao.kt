package com.example.ktjob.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProvinceDao {
    @Insert
    fun insert(province: ProvinceItem)

    @Delete
    fun delete(province: ProvinceItem)

    @Query("SELECT * FROM province")
    fun getAll(): List<ProvinceItem>

    @Query("DELETE FROM province")
    fun deleteAll()
}