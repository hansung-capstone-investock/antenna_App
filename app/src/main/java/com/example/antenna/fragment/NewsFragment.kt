package com.example.antenna.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antenna.R
import com.example.antenna.`interface`.MainNews
import com.example.antenna.adpater.MainNewsAdapter
import com.example.antenna.adpater.MainNewsList
import com.example.antenna.dataclass.NewsData
import kotlinx.android.synthetic.main.fragment_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsFragment : Fragment() {

    private val main_list = mutableListOf<MainNewsList>()
    private val main_adapter = MainNewsAdapter(main_list)

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstRetrofit: Retrofit = Retrofit.Builder()
               .baseUrl("http://ec2-13-125-236-101.ap-northeast-2.compute.amazonaws.com:8000/")
               .addConverterFactory(GsonConverterFactory.create())
               .build()
               val regionServer : MainNews? = firstRetrofit.create(MainNews::class.java)

        Log.d("GET REQUEST SUCCESS", firstRetrofit.toString())

        main_button.setOnClickListener {
            regionServer?.getNews()?.enqueue(object : Callback<List<NewsData>> {
                override fun onResponse(call: Call<List<NewsData>>, response: Response<List<NewsData>>) {
                    Log.d("GET NEWS SUCCESS", response.toString()) // 통신 성공 여부 확인 메세지

                    for(i in 0 until response.body()?.count()?.toInt()!!){

                        val title : String? = response.body()?.elementAt(i)?.title
                        val summary : String? = response.body()?.elementAt(i)?.summary
                        val publishDay : String? = response.body()?.elementAt(i)?.publishDay
                        var link : String? = response.body()?.elementAt(i)?.link // .. 클릭을 하면 URL 로 이동하게끔 구성하기

                        main_list.add(MainNewsList(title.toString(), summary.toString(), publishDay.toString(), link.toString()))
                    }
                    rv_main_data.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                    rv_main_data.adapter = main_adapter
                }
                override fun onFailure(call: Call<List<NewsData>>, t: Throwable) {
                    Log.d("GET NEWS Fail", t.toString())
                    t.printStackTrace()
                }

            })
        }

        live_button.setOnClickListener {
            rv_main_data.adapter = null
        }

//        main_button.isEnabled = true
    }

}