package com.example.antenna.`interface`

import com.example.antenna.dataclass.CommunityData
import com.example.antenna.dataclass.KoqData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CommunityService {
    @GET(" /views/api/dcList/")
    fun getCommunity(
            @Query("title") title: String? = null,
            @Query("count") count: Int? = null,
    ) : Call<List<CommunityData>>
}