package com.example.antenna.`interface`

import com.example.antenna.dataclass.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LiveNews {
    // API로 부터 메인 뉴스 정보 가져오기
    @GET("/views/api/livenews")
    fun getLiveNews(
        @Query("title") title: String? = null,
        @Query("summary") summary: String? = null,
        @Query("publishDate") publishDate: String? = null,
        @Query("link") link: String? = null
    ) : Call<List<LiveData>>
}