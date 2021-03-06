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
            @Query("close") close: String? = null,
    ) : Call<List<KosData>>
}