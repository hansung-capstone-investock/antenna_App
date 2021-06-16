package com.example.antenna

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.sharedPreference.App
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.antennaresult.*
import kotlinx.android.synthetic.main.backresult.*

class AntennaResult : AppCompatActivity() {

    // 데이터의 최고 최저 값
    private var dataMax : String? = null
    private var dataMin : String? = null

    private val date_list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.antennaresult)


        Log.e("actual :", App.prefs.actualAntennaGET().toString())
        Log.e("predict :", App.prefs.predictAntennaGET().toString())

        val thread = ThreadClass()
        thread.start()
    }

    inner class ThreadClass : Thread(){
        override fun run() {
            val dataList = Array<Double>(20) { Math.random() }

            dataMax = dataList.maxOrNull().toString()
            dataMin = dataList.minOrNull().toString()

            // Entry 배열
            val entries : ArrayList<Entry> = ArrayList()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, 0F))
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
            lineChart_antennaResult.data = data

            setChart()
            runOnUiThread {
                lineChart_antennaResult.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until dataList.size){
                // SystemClock.sleep(0.5.toLong())
                data.addEntry(Entry(i.toFloat(), dataList[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart_antennaResult.notifyDataSetChanged()
                lineChart_antennaResult.invalidate()
            }

            // isrunning = false
            super.run()
        }
    }

    private fun setChart(){

        // X축
        lineChart_antennaResult.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isGranularityEnabled = true

            valueFormatter = MyXAxisFormatter()
            setLabelCount(4, true)
        }

        lineChart_antennaResult.apply {
            // Y축
            axisRight.isEnabled = false
            axisLeft.axisMaximum = dataMax?.toFloat()!! + 1500F
            axisLeft.axisMinimum = dataMin?.toFloat()!! - 1500F
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
}