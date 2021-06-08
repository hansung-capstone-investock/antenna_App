package com.example.antenna.dataclass


import com.google.gson.annotations.SerializedName

data class CompanyData(
    val newsData: List<NewsDataX>,
    val stockData: List<StockData>
)