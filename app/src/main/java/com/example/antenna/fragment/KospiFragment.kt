package com.example.antenna.fragment

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.antenna.R
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_kosdaq.*

class KospiFragment : Fragment(){

    var isrunning = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_kospi, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

            for(i in input.indices){
                SystemClock.sleep(10)
                data.addEntry(Entry(i.toFloat(), input[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart.notifyDataSetChanged()
                lineChart.invalidate()
            }
            isrunning = false
            super.run()
        }
    }
}