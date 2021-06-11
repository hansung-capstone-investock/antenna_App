package com.example.antenna.interest

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.R
import com.example.antenna.`interface`.SearchService
import com.example.antenna.`interface`.UpdateService
import com.example.antenna.dataclass.CompanyData
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.company_main.*
import kotlinx.android.synthetic.main.company_main.lineChart1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import kotlin.math.round

class InterCompany : AppCompatActivity() {

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-125-236-101.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
            .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
            .build()
    private val searchService : SearchService = retrofit.create(SearchService::class.java)
    private val updateService : UpdateService = retrofit.create(UpdateService::class.java)

    private lateinit var code : String
    var companyData : CompanyData? = null
    var yesterday : Double? = null
    var today : Double? = null
    var percent : Double? = null
    var volume : Double? = null

    var cap : Double? = null
    var per : Double? = null
    var pbr : Double? = null

    // 1년치
    private var dataList = mutableListOf<Double>()


    // 3개월
    private var dataListMonth = mutableListOf<Double>()

    private var isrunningYear = false
    private var isrunningMonth = false

    // 데이터의 최고 최저 값0
    private var dataMax : Double? = null
    private var dataMin : Double? = null
    // 데이터 시작 값
    private var dataFirst : String? = null

    private val date_list = mutableListOf<String>()
    private val date_listMonth = mutableListOf<String>()
    // 스피너 목록
    // var dataArr

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_main)

        if(intent.hasExtra("name")){
            Log.d("HAVE NAME : ", intent.getStringExtra("name").toString())
            company_name.text = intent.getStringExtra("name")
            code = intent.getStringExtra("code").toString()
            company_code.text = code

            searchService.searchCompany(code).enqueue(object : Callback<CompanyData> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<CompanyData>, response: Response<CompanyData>) {

                    companyData = response.body()
                    // 숫자에 , 추가
                    val dec = DecimalFormat("#,###")

                    val count = companyData?.stockData?.count()
                    // 오늘 어제 종가
                    if (count != null) {
                        today = companyData?.stockData?.get(count - 2)?.close
                        yesterday = companyData?.stockData?.get(count - 3)?.close
                        volume = companyData?.stockData?.get(count - 2)?.volume

                        cap = companyData?.stockData?.get(count -2)?.cap
                        per = companyData?.stockData?.get(count -2)?.per
                        pbr = companyData?.stockData?.get(count -2)?.pbr

                        Log.e("today : ", today.toString())
                        Log.e("yesterday : ", yesterday.toString())
                        Log.e("volume : ", volume.toString())
                    }
                    // 오늘 - 어제 / 어제 * 100 등락률
                    percent = yesterday?.let { it1 -> today?.minus(it1) }?.div(yesterday!!)?.times(100)?.toDouble()
                    val percent1 = percent?.times(100)?.let { round(it) }?.div(100)

                    // 1년치 종가 데이터 가져오기
                    if (count != null) {
                        for(i in 0 until count-1){
                            val graphDataYear : Double? = companyData?.stockData?.elementAt(i)?.close
                            val graphDateYear : String? = companyData?.stockData?.elementAt(i)?.date
                            if (graphDataYear != null) {
                                dataList.add(graphDataYear)
                            }
                            if (graphDateYear != null){
                                val graphSub : String = graphDateYear.substring(2,10)
                                date_list.add(graphSub)
                            }
//                            Log.e("data list : ", dataList.toString())
                        }

                        // 3개월 데이터 자르기
                        for(i in count - 90 until count -1){
                            val graphDataMonth : Double? = companyData?.stockData?.elementAt(i)?.close
                            val graphDateMonth : String? = companyData?.stockData?.elementAt(i)?.date
                            if (graphDataMonth != null) {
                                dataListMonth.add(graphDataMonth)
                            }
                            if (graphDateMonth != null){
                                val graphSub : String = graphDateMonth.substring(2,10)
                                date_listMonth.add(graphSub)
                            }
                        }

                        dataMax = dataList.maxOrNull()
                        dataMin = dataList.minOrNull()
                        Log.e("data max : ", dataMax.toString())
                        Log.e("data min : ", dataMin.toString())

                        dataFirst = dataList[0].toInt().toString()
                        Log.e("dataFirst : ", dataFirst.toString())
                    }

                    company_rate.text = dec.format(today) + "원"
                    Fluctuation_rate.text = percent1.toString() + "%"

                    one_max.text = dec.format(dataMax)
                    one_min.text = dec.format(dataMin)

                    capvalue.text = dec.format(cap)
                    PER.text = dec.format(per)
                    PBR.text = dec.format(pbr)

                    trading_volume.text = dec.format(volume) + "주"

                    /*third_day.setOnClickListener {
                        isrunningYear = false
                        isrunningMonth = true
                        val thread = ThreadClassThird()
                        thread.start()
                    }*/

                   //  one_year.setOnClickListener {
                        isrunningMonth = false
                        isrunningYear = true
                        val thread = ThreadClass()
                        thread.start()
                    //}


                    if (percent1 != null) {
                        if(percent1 > 0.0){
                            company_rate.setTextColor(Color.RED)
                            Fluctuation_rate.setTextColor(Color.RED)
                        } else if (percent1 == 0.0){
                            company_rate.setTextColor(Color.BLACK)
                            Fluctuation_rate.setTextColor(Color.BLACK)
                        } else {
                            company_rate.setTextColor(Color.BLUE)
                            Fluctuation_rate.setTextColor(Color.BLUE)
                        }
                    }
                }

                override fun onFailure(call: Call<CompanyData>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        } else{
            Log.d("HAVEN NAME : ", intent.getStringExtra("name").toString())
        }

        /*AddCompany.setOnClickListener {
            updateService.UpdateCompany()
        }*/
    }

    // 1년 그래프
    inner class ThreadClass : Thread(){
        override fun run() {
            // Entry 배열
            val entries : ArrayList<Entry> = ArrayList()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, dataFirst?.toFloat()!!))
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
            runOnUiThread {
                lineChart1.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until dataList.size){
                // SystemClock.sleep(0.5.toLong())
                data.addEntry(Entry(i.toFloat(), dataList[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart1.notifyDataSetChanged()
                lineChart1.invalidate()
            }

            isrunningYear = false
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

            valueFormatter = MyXAxisFormatter()
            setLabelCount(4, true)
        }

        lineChart1.apply {
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

    // 3개월 그래프
    /*inner class ThreadClassThird : Thread(){
        override fun run() {
            // Entry 배열
            val entries : ArrayList<Entry> = ArrayList()
            // Entry 배열 초기값 입력
            entries.add(Entry(0F, dataFirst?.toFloat()!!))
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

            setChartMonth()
            runOnUiThread {
                lineChart1.animateXY(1, 1)
            }
            // 그래프 초기화 설정

            for(i in 0 until dataListMonth.size){
                // SystemClock.sleep(0.5.toLong())
                data.addEntry(Entry(i.toFloat(), dataListMonth[i].toFloat()), 0)
                data.notifyDataChanged()
                lineChart1.notifyDataSetChanged()
                lineChart1.invalidate()
            }

            isrunningMonth = false
            super.run()
        }
    }*/

    /*private fun setChartMonth(){

        // X축
        lineChart1.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isGranularityEnabled = true

            valueFormatter = MyXAxisFormatterMonth()
            setLabelCount(4, true)
        }

        lineChart1.apply {
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
    }*/

    // dataListMonth
    inner class MyXAxisFormatter : ValueFormatter(){

        // private val days = arrayOf("1차","2차","3차","4차","5차","6차","7차")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return date_list.getOrNull(value.toInt()) ?: value.toString()
        }
    }

    /*inner class MyXAxisFormatterMonth : ValueFormatter(){

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return date_listMonth.getOrNull(value.toInt()) ?: value.toString()
        }
    }*/
}