package com.example.antenna.`interface`

import com.example.antenna.dataclass.KoqData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CommunityService {
    @GET("/stock//")
    fun getKosdaq(
            @Query("") date: String? = null,
            @Query("") close: String? = null,
    ) : Call<List<KoqData>>
}