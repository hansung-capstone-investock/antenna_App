package com.example.antenna

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.sharedPreference.App
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.backresult.*

class BackResult : AppCompatActivity() {

    // 데이터의 최고 최저 값
    private var dataMax : String? = null
    private var dataMin : String? = null

    private var dateList = mutableListOf<Double>()

    private var dateListMax : Double? = null
    private var dateListMin : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backresult)

        dateList = App.prefs.backGET()

        /*dateListMax = dateList.maxOrNull().toString()
        dateListMin = dateList.minOrNull().toString()*/

        val thread = ThreadClass()
        thread.start()
    }

    inner class ThreadClass : Thread(){
        override fun run() {
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
            val data : LineData = LineData(dataSet)
            // activity_main에 배치된 lineChart에 데이터 연결 하기
            lineChart_backResult.data = data

            setChart()
            runOnUiThread {
                lineChart_backResult.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until dateList.size){
                // SystemClock.sleep(0.5.toLong())
                data.addEntry(Entry(i.toFloat(), dateList[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart_backResult.notifyDataSetChanged()
                lineChart_backResult.invalidate()
            }

            // isrunning = false
            super.run()
        }
    }

    private fun setChart(){

        // X축
        lineChart_backResult.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isGranularityEnabled = true
            setLabelCount(0, false)
        }

        lineChart_backResult.apply {
            // Y축
            axisRight.isEnabled = false
            axisLeft.axisMaximum = 60F
            axisLeft.axisMinimum = -20F
            setPinchZoom(false)
            description.isEnabled = false

            legend.apply {
                setDrawInside(false)
                isEnabled = false
            }
        }
    }
}