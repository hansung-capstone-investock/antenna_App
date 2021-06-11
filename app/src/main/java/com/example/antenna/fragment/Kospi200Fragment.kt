package com.example.antenna.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.antenna.R
import com.example.antenna.`interface`.Kos200Service
import com.example.antenna.dataclass.Kos200Data
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.fragment_kosdaq.*
import kotlinx.android.synthetic.main.fragment_kospi.*
import kotlinx.android.synthetic.main.fragment_kospi200.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.round

class Kospi200Fragment : Fragment(){

    private var isrunning3 = false

    // KOSPI 정보 데이터 넣기
    private val data_list = mutableListOf<Double>()
    private val date_list = mutableListOf<String>()

    private val kos200Retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-125-236-101.ap-northeast-2.compute.amazonaws.com:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val kos200Server : Kos200Service? = kos200Retrofit.create(Kos200Service::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_kospi200, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 서버로 부터 정보가져오기..
        kos200Server?.getKospi200()?.enqueue(object : Callback<List<Kos200Data>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<Kos200Data>>, response: Response<List<Kos200Data>>) {

                val count = response.body()?.count()
                val today = count?.minus(1)?.let { response.body()?.elementAt(it)?.close }
                val yesterday = count?.minus(2)?.let { response.body()?.elementAt(it)?.close }

                val rateChange = today?.toFloat()?.let { yesterday?.toFloat()?.minus(it) }?.toDouble()
                // 소수점 아래 3번째에서 반올림
                val rateChange1 = rateChange?.times(100)?.let { round(it) }?.div(100)

                val ratePercent = rateChange1?.div(yesterday!!)?.times(100)
                // 소수점 아래 3번째에서 반올림
                val ratePercent1 = ratePercent?.times(100)?.let { round(it) }?.div(100)

                Kospi200_value.text = today.toString()

                if (rateChange != null) {
                    if(rateChange > 0){
                        KOSPI200_Fluctuation.text = "▲$rateChange1"
                        Kospi200_value.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                        KOSPI200_Fluctuation.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                    }
                    else{
                        KOSPI200_Fluctuation.text = "▼$rateChange1"
                        Kospi200_value.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                        KOSPI200_Fluctuation.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                    }
                }

                if (ratePercent1 != null) {
                    if(ratePercent1 > 0){
                        Kospi200_percent.text = "+$ratePercent1%"
                        Kospi200_percent.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                    } else{
                        Kospi200_percent.text = "$ratePercent1%"
                        Kospi200_percent.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                    }
                }

                for(i in 0 until response.body()?.count()?.toInt()!!){
                    val graphData : Double? = response.body()?.elementAt(i)?.close
                    val graphDate : String? = response.body()?.elementAt(i)?.date
                    if (graphData != null) {
                        data_list.add(graphData)
                    }
                    if (graphDate != null){
                        val graphSub : String = graphDate.substring(2,10)
                        date_list.add(graphSub)
                    }
                }
                isrunning3 = true
                val thread = ThreadClass3()
                thread.start()
            }

            override fun onFailure(call: Call<List<Kos200Data>>, t: Throwable) {
                Log.d("GET KOSPI200 Fail", t.toString())
                t.printStackTrace()
            }
        })
    }

    inner class ThreadClass3 : Thread(){
        override fun run() {
            // Entry 배열
            val entries : ArrayList<Entry> = ArrayList()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, 285F))
            // 그래프 구현을 위한 LineDataSet 생성
            val dataSet : LineDataSet = LineDataSet(entries, "").apply {
                setDrawCircles(false)
                color = Color.RED
                highLightColor = Color.TRANSPARENT
                circleRadius = 0f
                valueTextSize = 0F
                lineWidth = 1.5F
            }
            // 그래프 data 생성 -> 최종입력 데이터
            val data : LineData = LineData(dataSet)
            // activity_main에 배치된 lineChart에 데이터 연결 하기
            lineChart3.data = data

            setChart()
            activity?.runOnUiThread {
                lineChart3.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until data_list.size){
                // SystemClock.sleep(0.5.toLong())
                data.addEntry(Entry(i.toFloat(), data_list[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart3.notifyDataSetChanged()
                lineChart3.invalidate()
            }

            isrunning3 = false
            super.run()
        }
    }

    private fun setChart(){

        // X축
        val xAxis = lineChart3.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isGranularityEnabled = true

            valueFormatter = MyXAxisFormatter()
            setLabelCount(4, true)
        }

        lineChart3.apply {
            // Y축
            axisRight.isEnabled = false
            axisLeft.axisMaximum = 450f
            axisLeft.axisMinimum = 220f
            setPinchZoom(false)
            description.isEnabled = false

            legend.apply {
                setDrawInside(false)
                isEnabled = false
            }
        }
    }

    inner class MyXAxisFormatter : ValueFormatter(){

        // private val days = arrayOf("1차","2차","3차","4차","5차","6차","7차")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return date_list.getOrNull(value.toInt()) ?: value.toString()
        }
    }

    override fun onResume() {
        super.onResume()

    }
}