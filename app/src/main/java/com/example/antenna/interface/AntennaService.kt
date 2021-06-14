package com.example.antenna.`interface`

import com.example.antenna.dataclass.AntennaData
import retrofit2.Call
import retrofit2.http.*

interface AntennaService {
    @FormUrlEncoded
    @POST("/tensor/antenna/")
    fun requestAntenna(
            @Field("companyCode") companyCode: String,
            @Field("indicator") indicator: ArrayList<String>,
            @Field("predictDate") predictDate: Int
    ) : Call<AntennaData>
}