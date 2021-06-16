package com.example.antenna.`interface`

import com.example.antenna.dataclass.AntennaData
import com.example.antenna.dataclass.AtennaInfo
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface AntennaService {
    @POST("/tensor/antenna/")
    fun requestAntenna(
        @Body() antennaInfo : AtennaInfo
    ) : Call<AntennaData>
}