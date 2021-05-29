package com.example.antenna.fragment

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.antenna.R
import com.example.antenna.`interface`.KosService
import com.example.antenna.dataclass.KosData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_kosdaq.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KospiFragment : Fragment(){

    var isrunning = false

    val data_list = mutableListOf<Double>()

    private val kosRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-125-236-101.ap-northeast-2.compute.amazonaws.com:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val kosServer : KosService? = kosRetrofit.create(KosService::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_kospi, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 서버로 부터 정보가져오기..
        kosServer?.getKospi()?.enqueue(object : Callback<List<KosData>>{
            override fun onResponse(call: Call<List<KosData>>, response: Response<List<KosData>>) {
                Log.d("GET KOSPI SUCCESS", response.toString())

                for(i in 0 until response.body()?.count()?.toInt()!!){
                    val graph_data : Double? = response.body()?.elementAt(i)?.close

                    if (graph_data != null) {
                        data_list.add(graph_data)
                        // Log.d("graph data : ", response.body()?.count().toString())
                        // data_list를 가지고 그래프를 그리기
                    }
                }
            }

            override fun onFailure(call: Call<List<KosData>>, t: Throwable) {
                Log.d("GET KOSPI Fail", t.toString())
                t.printStackTrace()
            }
        })

        isrunning = true
        val thread = ThreadClass()
        thread.start()
    }

    override fun onResume() {
        super.onResume()
    }

    inner class ThreadClass : Thread(){
        override fun run() {
            val input = Array<Double>(10) { Math.random() }
            // Entry 배열
            val entries : ArrayList<Entry> = ArrayList()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, 0F))
            // 그래프 구현을 위한 LineDataSet 생성
            var dataset : LineDataSet = LineDataSet(entries, "")
            // 그래프 data 생성 -> 최종입력 데이터
            var data : LineData = LineData(dataset)
            // activity_main에 배치된 lineChart에 데이터 연결 하기
            lineChart.data = data

            activity?.runOnUiThread {
                lineChart.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until data_list.size){
                SystemClock.sleep(10)
                data.addEntry(Entry(i.toFloat(), data_list[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart.notifyDataSetChanged()
                lineChart.invalidate()
            }
            
            isrunning = false
            super.run()
        }
    }
}