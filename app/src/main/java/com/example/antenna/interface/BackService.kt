package com.example.antenna.`interface`

import com.example.antenna.dataclass.AntennaData
import com.example.antenna.dataclass.BackData
import retrofit2.Call
import retrofit2.http.*

interface BackService {
    @FormUrlEncoded
    @POST("/stock/bt/")
    fun requestBack(
            @Field("start") start: String,
            @Field("end") end: String,
            @Field("market") market: ArrayList<Int>,
            @Field("sector") sector: ArrayList<Int>,
            @Field("conditions") conditions: List<ArrayList<Int>>,
            @Field("sellCondition") sellCondition: ArrayList<String>,
    ) : Call<BackData>
}