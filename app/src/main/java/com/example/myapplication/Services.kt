package com.example.myapplication

//data class Coordinates(
//    val longitude: Double,
//    val latitude: Double
//)
//data class Geometry(
//    val type: String,
//    val coordinates: List<List<Coordinates>>
//)

data class Service(
    val id: Int,
    val name: String,
    val keywords: String,
    val geometry: String
)
