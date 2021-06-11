package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class StockData(
    val close: Double,
    val date: String,
    val high: Double,
    val low: Double,
    val open: Double,
    val volume: Double,
    val cap : Double,
    val per : Double,
    val pbr : Double,
    val psr : Double,
    val roe : Double,
    val roa : Double,
)