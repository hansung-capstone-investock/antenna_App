package com.example.antenna.`interface`

import com.example.antenna.dataclass.SignData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SignUpService {
    @FormUrlEncoded
    @POST("/account/api/signup/")
    fun requestsignup(
        // Input Data 정의
        @Field("id") id: String,
        @Field("password") password: String
    ) : Call<SignData> // Output Data 정의
}