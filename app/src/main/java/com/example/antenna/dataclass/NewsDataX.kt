package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class NewsDataX(
    val link: String,
    val publishDay: String,
    val summary: String,
    val title: String
)