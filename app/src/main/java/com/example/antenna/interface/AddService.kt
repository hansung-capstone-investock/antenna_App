package com.example.antenna.`interface`

import com.example.antenna.dataclass.InterAddData
import retrofit2.Call
import retrofit2.http.*

interface AddService {
    @FormUrlEncoded
    @POST("/account/api/interestUpdate/")
    fun requestCompany(
            // 사용자에게 관심기업 추가하는~~~
            @Field("name") name: String,
            @Field("group") group : String? = null,
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
    ) : Call<InterAddData>
}