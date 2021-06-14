package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class UpdateDataItem(
    val companies: CompaniesX,
    val group: String,
    val id: Int,
    val name: String
)