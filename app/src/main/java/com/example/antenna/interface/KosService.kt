package com.example.antenna.`interface`

import com.example.antenna.dataclass.KosData
import com.example.antenna.dataclass.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KosService {
    @GET("/stock/kospiyear/")
    fun getKospi(
            @Query("date") date: String? = null,
            @Query("high") high: String? = null,
            @Query("close") close: String? = null,
            @Query("tradingVolume") tradingVolume: String? = null,
            @Query("tradingPrice") tradingPrice: String? = null
    ) : Call<List<KosData>>
}