package com.example.antenna.interest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.MainActivity
import com.example.antenna.R
import com.example.antenna.`interface`.FirstService
import com.example.antenna.adpater.RecyclerAdapter
import com.example.antenna.dataclass.FirstData
import com.example.antenna.fragment.MainFragment
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class InterestActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_company)

        val intent = Intent(this, MainFragment::class.java)
        val name = intent.getStringExtra("name")
        Log.d("USER NAME", name.toString())


        // Retrofit 연결을 통해 초기값 가져오기
        /*val firstRetrofit: Retrofit = Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                var regionServer : FirstService? = firstRetrofit.create(FirstService::class.java)

        regionServer?.getInfo("json", "")?.enqueue(object : Callback<FirstData>{
            override fun onResponse(call: Call<FirstData>, response: Response<FirstData>) {
                var name = response.body()?.body?.name
                var address = response.body()?.body?.address
                var phoneNum = response.body()?.body?.phone_number

            }

            override fun onFailure(call: Call<FirstData>, t: Throwable) {
                t.printStackTrace()
            }

        })*/
    }


}