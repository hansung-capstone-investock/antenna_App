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
            @Field("company1") company1: String? = null,
            @Field("company2") company2: String? = null,
            @Field("company3") company3: String? = null,
            @Field("company4") company4: String? = null,
            @Field("company5") company5: String? = null,
            @Field("company6") company6: String? = null,
            @Field("company7") company7: String? = null,
            @Field("company8") company8: String? = null,
            @Field("company9") company9: String? = null,
            @Field("company10") company10: String? = null
    ) : Call<UpdateData>
}