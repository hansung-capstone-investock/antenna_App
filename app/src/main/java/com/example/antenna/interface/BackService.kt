package com.example.antenna.`interface`

import com.example.antenna.dataclass.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface BackService {
    @POST("/stock/btApp/")
    fun requestBack(
        @Body() backInfo : BackDataX
    ) : Call<ResponseBody>
}