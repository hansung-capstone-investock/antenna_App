package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class BackDataX(
        val id: String,
        val start: String,
        val end: String,
        val market: List<Int>,
        val sellCondition: List<Int>,
        val sector: List<Int>,
        val per: List<Int>,
        val pbr: List<Int>,
        val psr: List<Int>,
        val roe: List<Int>,
        val roa: List<Int>
)