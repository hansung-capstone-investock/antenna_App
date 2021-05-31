package com.example.antenna.`interface`

import com.example.antenna.dataclass.KoqData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KoqService {
    @GET("/stock/kosdaqyear/")
    fun getKosdaq(
            @Query("date") date: String? = null,
            @Query("close") close: String? = null,
    ) : Call<List<KoqData>>
}