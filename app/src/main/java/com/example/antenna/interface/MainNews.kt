package com.example.antenna.`interface`

import com.example.antenna.dataclass.NewsData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface MainNews {
    // API로 부터 메인 뉴스 정보 가져오기
    @GET("/views/api/news")
    fun getNews(
            @Query("title") title: String? = null,
            @Query("summary") summary: String? = null,
            @Query("publishDay") publishDay: String? = null,
            @Query("link") link: String? = null
        ) : Call<List<NewsData>>
}