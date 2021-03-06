package com.example.antenna.`interface`

import com.example.antenna.dataclass.LoadData
import retrofit2.Call
import retrofit2.http.*

interface LoadService {
    @FormUrlEncoded
    @POST("/account/api/intereststock/")
    fun requestCompany(
            // 사용자에게 관심기업 추가하는~~~
            @Field("name") name: String
    ) : Call<LoadData>
}