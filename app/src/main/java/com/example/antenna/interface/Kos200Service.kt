package com.example.antenna.`interface`

import com.example.antenna.dataclass.Kos200Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Kos200Service {
    @GET("/stock/kospi200year/")
    fun getKospi200(
            @Query("date") date: String? = null,
            @Query("close") close: String? = null,
    ) : Call<List<Kos200Data>>
}