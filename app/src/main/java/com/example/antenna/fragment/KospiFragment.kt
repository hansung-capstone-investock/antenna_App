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
import com.example.antenna.`interface`.KosService
import com.example.antenna.dataclass.KosData
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_kospi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.round
import kotlin.math.roundToInt

class KospiFragment : Fragment(){

    private var isrunning = false

    // KOSPI 정보 데이터 넣기
    private val data_list = mutableListOf<Double>()

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
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<KosData>>, response: Response<List<KosData>>) {

                val count = response.body()?.count()
                // 자료형은 Double
                val today = count?.minus(1)?.let { response.body()?.elementAt(it)?.close }
                val yesterday = count?.minus(2)?.let { response.body()?.elementAt(it)?.close }

                val rateChange = today?.toFloat()?.let { yesterday?.toFloat()?.minus(it) }?.toDouble()
                // 소수점 아래 3번째에서 반올림
                val rateChange1 = rateChange?.times(100)?.let { round(it) }?.div(100)

                val ratePercent = rateChange1?.div(yesterday!!)?.times(100)
                // 소수점 아래 3번째에서 반올림
                val ratePercent1 = ratePercent?.times(100)?.let { round(it) }?.div(100)

                Kospi_value.text = today.toString()

                if (rateChange1 != null) {
                    if(rateChange1 > 0){
                        Kospi_Fluctuation.text = "▲$rateChange1"
                        Kospi_value.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                        Kospi_Fluctuation.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                    }
                    else{
                        Kospi_Fluctuation.text = "▼$rateChange1"
                        Kospi_value.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                        Kospi_Fluctuation.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                    }
                }

                if (ratePercent1 != null) {
                    if(ratePercent1 > 0){
                        Kospi_percent.text = "+$ratePercent1%"
                        Kospi_percent.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                    } else{
                        Kospi_percent.text = "$ratePercent1%"
                        Kospi_percent.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                    }
                }


                for(i in 0 until response.body()?.count()?.toInt()!!){
                    val graphData : Double? = response.body()?.elementAt(i)?.close

                    if (graphData != null) {
                        data_list.add(graphData)
                    }
                }
                isrunning = true
                val thread = ThreadClass()
                thread.start()
            }

            override fun onFailure(call: Call<List<KosData>>, t: Throwable) {
                Log.d("GET KOSPI Fail", t.toString())
                t.printStackTrace()
            }
        })
    }

    inner class ThreadClass : Thread(){
        override fun run() {
            // Entry 배열
            val entries : ArrayList<Entry> = ArrayList()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, 2147F))
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
            var data : LineData = LineData(dataSet)
            // activity_main에 배치된 lineChart에 데이터 연결 하기
            lineChart1.data = data

            setChart()
            activity?.runOnUiThread {
                lineChart1.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until data_list.size){
                // SystemClock.sleep(0.5.toLong())
                data.addEntry(Entry(i.toFloat(), data_list[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart1.notifyDataSetChanged()
                lineChart1.invalidate()
            }
            
            isrunning = false
            super.run()
        }
    }

    private fun setChart(){

        // X축
        lineChart1.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isGranularityEnabled = true
            textColor = Color.TRANSPARENT
        }

        lineChart1.apply {
            // Y축
            axisRight.isEnabled = false
            axisLeft.axisMaximum = 3500f
            axisLeft.axisMinimum = 1800f
            setPinchZoom(false)
            description.isEnabled = false

            legend.apply {
                setDrawInside(false)
                isEnabled = false
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}