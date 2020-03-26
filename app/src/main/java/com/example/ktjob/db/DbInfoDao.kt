package com.example.ktjob.db

import androidx.room.*

@Dao
interface DbInfoDao {
    @Insert
    fun insert(info: DbInfo)

    @Update
    fun update(info: DbInfo)

    @Query("SELECT * FROM dbinfo")
    fun get(): List<DbInfo>

    @Query("DELETE FROM dbinfo")
    fun deleteAll()
}