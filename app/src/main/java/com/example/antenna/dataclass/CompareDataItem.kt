package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class CompareDataItem(
    val company: String,
    val date: String,
    val gap: Double,
    val rank: Int,
    val stockcode: String
)