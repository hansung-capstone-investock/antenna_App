package com.example.antenna.`interface`

import com.example.antenna.dataclass.FirstData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FirstService {
    @GET("")
    fun getInfo(@Query("Type")Type:String,
                @Query("KEY")KEY:String,
                @Query("pIndex")pIndex:Int? = null,
                @Query("pSize")pSize:Int? = null) : Call<FirstData>
}