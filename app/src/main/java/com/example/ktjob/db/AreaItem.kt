package com.example.ktjob.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ktjob.json.Area
import com.example.ktjob.json.City
import com.example.ktjob.json.Province

@Entity(tableName = "area")
class AreaItem constructor(pCode: String, pName: String,
                           cCode: String, cName: String,
                           aCode: String, aName: String) {
    @ColumnInfo(name = "province_code")
    var provinceCode: String = pCode

    @ColumnInfo(name = "province_name")
    var provinceName: String = pName

    @ColumnInfo(name = "city_code")
    var cityCode: String = cCode

    @ColumnInfo(name = "city_name")
    var cityName: String = cName

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "area_code")
    var areaCode: String = aCode

    @ColumnInfo(name = "area_name")
    var areaName: String = aName

    constructor(province: Province, city: City, area: Area): this(province.code,
            province.name, city.code, city.name, area.code, area.name)

    constructor() : this("", "", "", "", "", "")
}