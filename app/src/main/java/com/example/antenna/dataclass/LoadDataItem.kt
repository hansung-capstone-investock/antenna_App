package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class LoadDataItem(
    val companies: Companies,
    val group: Any,
    val name: String,
    val id: Int
)