package com.example.antenna

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.sharedPreference.App
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.antennaresult.*
import kotlinx.android.synthetic.main.backresult.*
import kotlinx.android.synthetic.main.fragment_kospi200.*

class AntennaResult : AppCompatActivity() {

    // 데이터의 최고 최저 값
    private var actualDataMax : String? = null
    private var actualDataMin : String? = null
    private var predictDataMax : String? = null
    private var predictDataMin : String? = null

    private var actualList = mutableListOf<Double>()
    private var predictList = mutableListOf<Double>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.antennaresult)

        actualList =  App.prefs.actualAntennaGET()
        predictList =  App.prefs.predictAntennaGET()

        val actualCount : Int = actualList.count()
        val predictCount : Int = predictList.count()

        Log.d("actualList : ", actualList.toString())
        Log.d("predictList : ", predictList.toString())

        Log.d("actualCount : ", actualCount.toString())
        Log.d("predictCount : ", predictCount.toString())

        actualLast.text = "실제 데이터 마지막 값 : " + actualList[actualCount - 1].toInt().toString()
        actualLast.setTextColor(Color.RED)
        predictLast.text = "예측 데이터 마지막 값 : " + predictList[predictCount - 1].toInt().toString()
        predictLast.setTextColor(Color.BLUE)

        actualDataMax = actualList.maxOrNull().toString()
        actualDataMin = actualList.minOrNull().toString()

        predictDataMax = predictList.maxOrNull().toString()
        predictDataMin = predictList.minOrNull().toString()


        val thread = ThreadClass()
        thread.start()

        val predictThread = ThreadClassPredict()
        predictThread.start()
    }

    inner class ThreadClass : Thread(){
        override fun run() {
            // Entry 배열
            val entries : ArrayList<Entry> = ArrayList()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, actualList[0].toFloat()))
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
            lineChart_antennaResult.data = data

            setChart()
            runOnUiThread {
                lineChart_antennaResult.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until actualList.size){
                // SystemClock.sleep(0.5.toLong())
                data.addEntry(Entry(i.toFloat(), actualList[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart_antennaResult.notifyDataSetChanged()
                lineChart_antennaResult.invalidate()
            }
            super.run()
        }
    }

    inner class ThreadClassPredict : Thread(){
        override fun run() {
            // Entry 배열
            val entries : ArrayList<Entry> = ArrayList()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, predictList[0].toFloat()))
            // 그래프 구현을 위한 LineDataSet 생성
            val dataSet : LineDataSet = LineDataSet(entries, "").apply {
                setDrawCircles(false)
                color = Color.BLUE
                highLightColor = Color.TRANSPARENT
                circleRadius = 0f
                valueTextSize = 0F
                lineWidth = 1.5F
            }
            // 그래프 data 생성 -> 최종입력 데이터
            val data : LineData = LineData(dataSet)
            // activity_main에 배치된 lineChart에 데이터 연결 하기
            predictResult.data = data

            setChartPredict()
            runOnUiThread {
                predictResult.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until predictList.size){
                // SystemClock.sleep(0.5.toLong())
                data.addEntry(Entry(i.toFloat(), predictList[i].toFloat()), 0)
                data.notifyDataChanged()
                predictResult.notifyDataSetChanged()
                predictResult.invalidate()
            }
            super.run()
        }
    }

    private fun setChart(){

        // X축
        val xAxis = lineChart_antennaResult.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isGranularityEnabled = true

            setLabelCount(4, true)
        }

        lineChart_antennaResult.apply {
            // Y축
            axisRight.isEnabled = false
            axisLeft.axisMaximum = actualDataMax?.toFloat()!! + 1500F
            axisLeft.axisMinimum = actualDataMin?.toFloat()!! - 1500F
            setPinchZoom(false)
            description.isEnabled = false

            legend.apply {
                setDrawInside(false)
                isEnabled = false
            }
        }
    }

    private fun setChartPredict(){

        // X축
        val xAxis = predictResult.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isGranularityEnabled = true

            setLabelCount(4, true)
        }

        predictResult.apply {
            // Y축
            axisRight.isEnabled = false
            axisLeft.axisMaximum = predictDataMax?.toFloat()!! + 1500F
            axisLeft.axisMinimum = predictDataMin?.toFloat()!! - 1500F
            setPinchZoom(false)
            description.isEnabled = false

            legend.apply {
                setDrawInside(false)
                isEnabled = false
            }
        }
    }
}