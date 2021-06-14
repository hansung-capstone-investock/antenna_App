package com.example.antenna.`interface`

import com.example.antenna.dataclass.CompareData
import com.example.antenna.dataclass.KoqData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CompareService {
    @GET("/stock/compare3/")
    fun getCompare(
            @Query("date") date: String? = null,
            @Query("stockcode") stockcode: String? = null,
            @Query("company") company: String? = null,
            @Query("gap") gap: Double? = null,
            @Query("rank") rank: Int? = null,
    ) : Call<CompareData>
}