package com.example.antenna.`interface`

import com.example.antenna.dataclass.CompanyData
import retrofit2.Call
import retrofit2.http.*

interface SearchService {
    @FormUrlEncoded
    @POST("/stock/stocksearch/")
    fun searchCompany(
            // 기업 정보 가져오는..
            @Field("companyCode") companyCode: String
    ) : Call<CompanyData>
}