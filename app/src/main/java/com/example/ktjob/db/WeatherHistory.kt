package com.example.ktjob.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "history")
class WeatherHistory constructor(var aCode: String, var aName: String,
                                 var cond: String, var condCode: String,
                                 var temp: String, var stamp: String){
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "area_code")
    var areaCode: String = aCode

    @ColumnInfo(name = "area_name")
    var areaName: String = aName

    @ColumnInfo(name = "weather_condition")
    var condition: String = cond

    @ColumnInfo(name = "weather_condcode")
    var conditionCode: String = condCode

    @ColumnInfo(name = "temperature")
    var temperature: String = temp

    @ColumnInfo(name = "timestamp")
    var timestamp: String = stamp
}