package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class UpdateDataItem(
    val companies: Companies,
    val group: Any,
    val name: String
)