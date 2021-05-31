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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_kosdaq.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

class KospiFragment : Fragment(){

    var isrunning = false

    val data_list = mutableListOf<Double>()

    private val kosRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-125-236-101.ap-northeast-2.compute.amazonaws.com:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val kosServer : KosService? = kosRetrofit.create(KosService::class.java)

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

    private fun setChart(){
        val xAxis = lineChart.xAxis

        xAxis.apply {
            position =  XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            granularity = 1f
            axisMinimum = 2f
            isGranularityEnabled = true
        }

        lineChart.apply {
            axisRight.isEnabled = false
            axisLeft.axisMaximum = 50f
            legend.apply {
                textSize = 15f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
        }
        val lineData = LineData()
        lineChart.data = lineData
//        feedMultiple()
    }
    
    // 차트에 쓰일 데이터 목록 가져오기
    private fun feedMultiple() {
        if (thread != null){
            thread!!.interrupt()
        }

        val runnable = Runnable {
            addEntry()
        }

        thread = Thread(Runnable {
            while (true){
                runOnUiThread(runnable)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException){
                    e.printStackTrace()
                }
            }
        })
        thread!!.start()
    }

    private fun addEntry() {
        val data = lineChart.data

        data?.let {
            var set: ILineDataSet? = data.getDataSetByIndex(0)

            if(set == null){
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(Entry(set.entryCount.toFloat(), floatTemp), 0)

            data.notifyDataChanged()
            lineChart.apply {
                notifyDataSetChanged()
                moveViewToX(data.entryCount.toFloat())
                setVisibleXRangeMaximum(4f)

            }
        }
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