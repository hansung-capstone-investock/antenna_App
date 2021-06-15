package com.example.antenna.`interface`

import com.example.antenna.dataclass.BackData
import com.example.antenna.dataclass.sectorData
import retrofit2.Call
import retrofit2.http.*

interface BackService {
    @FormUrlEncoded
    @POST("/stock/bt/")
    fun requestBack(
            @Field("start") start: String,
            @Field("end") end: String,
            @Field("market") market: List<Int>,
            @Field("sector") sector: List<Int>,
            @Field("conditions") conditions: List<sectorData>,
            @Field("sellCondition") sellCondition: List<Int>,
    ) : Call<BackData>
}