package com.example.ktjob.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ktjob.json.Province

@Entity(tableName = "dbinfo")
class DbInfo constructor(init : Boolean) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "initialized")
    var initialized: Boolean = init

    constructor() : this(true)
}