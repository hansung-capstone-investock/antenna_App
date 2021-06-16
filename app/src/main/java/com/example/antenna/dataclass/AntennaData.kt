package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class AntennaData(
    val `actual`: Actual,
    val predict: Predict
)