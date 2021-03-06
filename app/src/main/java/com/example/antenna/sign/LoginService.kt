package com.example.antenna.sign


import com.example.antenna.dataclass.LoginData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService{

    @FormUrlEncoded
    @POST("/account/api/login/")
    // 실제 로그인하는 서비스
        fun requestLogin(
            // Input Data 정의
            @Field("id") userid: String,
            @Field("password") userpw: String
        ) : Call<LoginData> // Output Data 정의


        // 다른 서버스 함수 추가하면됨
}
