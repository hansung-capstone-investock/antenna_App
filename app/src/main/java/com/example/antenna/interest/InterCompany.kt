package com.example.antenna.interest

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.antenna.R
import com.example.antenna.`interface`.SearchService
import com.example.antenna.`interface`.UpdateService
import com.example.antenna.dataclass.CompanyData
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.company_main.*
import kotlinx.android.synthetic.main.company_main.lineChart1
import kotlinx.android.synthetic.main.fragment_kospi.*
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

    private var dataList = mutableListOf<Double>()
    private var isrunning = false

    // 데이터의 최고 최저 값
    private var dataMax : String? = null
    private var dataMin : String? = null
    // 데이터 시작 값
    private var dataFirst : String? = null

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
                override fun onResponse(call: Call<CompanyData>, response: Response<CompanyData>) {

                    companyData = response.body()
                    // 숫자에 , 추가
                    val dec = DecimalFormat("#,###")

                    val count = companyData?.stockData?.count()
                    // 오늘 어제 종가
                    if (count != null) {
                        today = companyData?.stockData?.get(count - 2)?.close
                        yesterday = companyData?.stockData?.get(count - 3)?.close
                    }
                    // 오늘 - 어제 / 어제 * 100 등락률
                    percent = yesterday?.let { it1 -> today?.minus(it1) }?.div(yesterday!!)?.times(100)?.toDouble()
                    val percent1 = percent?.times(100)?.let { round(it) }?.div(100)

                    // 1년치 종가 데이터 가져오기
                    if (count != null) {
                        for(i in 0 until count-1){
                            val graphData : Double? = companyData?.stockData?.elementAt(i)?.close
                            if (graphData != null) {
                                dataList.add(graphData)
                            }
//                            Log.e("data list : ", dataList.toString())
                        }
                        dataMax = dataList.maxOrNull().toString()
                        dataMin = dataList.minOrNull().toString()
                        Log.e("data max : ", dataMax.toString())
                        Log.e("data min : ", dataMin.toString())

                        Log.e("data last : ", dataList[count - 2].toString())

                        dataFirst = dataList[0].toInt().toString()
                        Log.e("dataFirst : ", dataFirst.toString())
                    }

                    // 오늘 최고가
                    val todayMax = companyData?.stockData?.get(342)?.high?.toInt()
                    // 오늘 최저가
                    val todayMin = companyData?.stockData?.get(342)?.low?.toInt()
                    // 오늘 거래량
                    val volume = companyData?.stockData?.get(342)?.volume?.toInt()
                    // 시작 값


                    company_rate.text = dec.format(today)
                    Fluctuation_rate.text = percent1.toString()
                    one_max.text = dec.format(todayMax)
                    one_min.text = dec.format(todayMin)
                    trading_volume.text = dec.format(volume)

                    isrunning = true
                    val thread = ThreadClass()
                    thread.start()

                    /*if (percent1 != null) {
                        if(percent1 > 0.0){
                            company_rate.setTextColor(Color.parseColor(R.color.red.toString()))
                            Fluctuation_rate.setTextColor(Color.parseColor(R.color.red.toString()))
                        } else if (percent1 == 0.0){
                            company_rate.setTextColor(Color.parseColor(R.color.black.toString()))
                            Fluctuation_rate.setTextColor(Color.parseColor(R.color.black.toString()))
                        } else {
                            company_rate.setTextColor(Color.parseColor(R.color.blue.toString()))
                            Fluctuation_rate.setTextColor(Color.parseColor(R.color.blue.toString()))
                        }
                    }*/
                }

                override fun onFailure(call: Call<CompanyData>, t: Throwable) {
                    t.printStackTrace()
                }
            })

            /*if(intent.getStringExtra("percent")?.toDouble()!! > 0.0){
                Log.e("HAVE PERCENT HIGH : ", intent.getStringExtra("percent").toString())
                company_rate.setTextColor(ContextCompat.getColor(this, R.color.red))
                Fluctuation_rate.setTextColor(ContextCompat.getColor(this, R.color.red))
            } else if(intent.getStringExtra("percent")?.toDouble()!! == 0.0){
                Log.e("HAVE PERCENT HIGH : ", intent.getStringExtra("percent").toString())
                company_rate.setTextColor(ContextCompat.getColor(this, R.color.black))
                Fluctuation_rate.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
            else{
                Log.e("HAVE PERCENT LOW : ", intent.getStringExtra("percent").toString())
                company_rate.setTextColor(ContextCompat.getColor(this, R.color.blue))
                Fluctuation_rate.setTextColor(ContextCompat.getColor(this, R.color.blue))
            }*/

        } else{
            Log.d("HAVEN NAME : ", intent.getStringExtra("name").toString())
        }

        /*AddCompany.setOnClickListener {
            updateService.UpdateCompany()
        }*/
    }

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
}