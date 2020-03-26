package com.example.ktjob.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ktjob.json.Province

@Entity(tableName = "province")
class ProvinceItem constructor(pCode: String, pName: String) {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "code")
    var code: String = pCode

    @ColumnInfo(name = "name")
    var name: String = pName

    constructor(province: Province): this(province.code, province.name)

    constructor() : this("", "")
}