package com.example.antenna.`interface`

import com.example.antenna.dataclass.InterData
import retrofit2.Call
import retrofit2.http.*

interface InterService {
    @FormUrlEncoded
    @POST("/account/api/interestUpdate/")
    fun request_interest(
            // Input Data 정의
            @Field("name") name: String,
            @Field("company1") company1: ArrayList<String>,
            @Field("group") group : String
    ) : Call<InterData>
}