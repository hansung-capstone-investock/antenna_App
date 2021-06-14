package com.example.antenna.`interface`

import com.example.antenna.dataclass.LoadData
import com.example.antenna.dataclass.UpdateData
import retrofit2.Call
import retrofit2.http.*

interface UpdateService {
    @FormUrlEncoded
    @POST("/account/api/interestUpdate/")
    fun updateCompany(
            @Field("id") id: Int,
            @Field("name") name: String,
            @Field("group") group: String,
            @Field("company1") company1: String,
            @Field("company2") company2: String,
            @Field("company3") company3: String,
            @Field("company4") company4: String,
            @Field("company5") company5: String,
            @Field("company6") company6: String,
            @Field("company7") company7: String,
            @Field("company8") company8: String,
            @Field("company9") company9: String,
            @Field("company10") company10: String,
    ) : Call<UpdateData>
}