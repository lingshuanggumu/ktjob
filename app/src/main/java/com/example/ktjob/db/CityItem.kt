package com.example.ktjob.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ktjob.json.City
import com.example.ktjob.json.Province

@Entity(tableName = "city")
class CityItem constructor(pCode: String, pName: String, cCode: String, cName: String) {
    @ColumnInfo(name = "province_code")
    var provinceCode: String = pCode

    @ColumnInfo(name = "province_name")
    var provinceName: String = pName

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "city_code")
    var cityCode: String = cCode

    @ColumnInfo(name = "city_name")
    var cityName: String = cName

    constructor(province: Province, city: City): this(province.code, province.name,
        city.code, city.name)

    constructor() : this("", "", "", "")
}