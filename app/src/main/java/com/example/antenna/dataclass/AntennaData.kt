package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class AntennaData(
        @SerializedName("actual")
        var actual: Actual,
        @SerializedName("predict")
        var predict: Predict
)