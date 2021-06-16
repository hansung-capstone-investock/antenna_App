package com.example.antenna.`interface`

import com.example.antenna.dataclass.*
import retrofit2.Call
import retrofit2.http.*

interface BackService {
    @POST("/stock/bt/")
    fun requestBack(
        @Body() backInfo : BackInfo
    ) : Call<BackData>
}