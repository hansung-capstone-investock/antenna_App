package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class AntennaData(
        val predict: Predict,
        val actual : Actual
)