package com.example.ktjob.json

import androidx.room.Entity

data class Location(
    val province: List<Province>
)

data class Province(
    val city: List<City>,
    val code: String,
    val name: String
)

data class City(
    val area: List<Area>,
    val code: String,
    val name: String
)

data class Area(
    val code: String,
    val name: String
)