package com.example.antenna.interest

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.R
import com.example.antenna.`interface`.SearchService
import com.example.antenna.`interface`.UpdateService
import com.example.antenna.adpater.DataList
import com.example.antenna.dataclass.CompanyData
import com.example.antenna.dataclass.UpdateData
import com.example.antenna.fragment.AntennaFragment
import com.example.antenna.fragment.MyFragment
import com.example.antenna.sharedPreference.App
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.company_main.*
import kotlinx.android.synthetic.main.company_main.lineChart1
import kotlinx.android.synthetic.main.fragment_antenna.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import kotlin.math.round

class InterCompany : AppCompatActivity() {

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
            .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
            .build()
    private val searchService : SearchService = retrofit.create(SearchService::class.java)
    private val updateService : UpdateService = retrofit.create(UpdateService::class.java)

    private var companyname : String? = null
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

    private var isrunningYear = false

    // 데이터의 최고 최저 값0
    private var dataMax : Double? = null
    private var dataMin : Double? = null
    // 데이터 시작 값
    private var dataFirst : String? = null

    private val date_list = mutableListOf<String>()

    var groupId : Int? = null
    var groupName : String? = null
    private val listGruop1 = mutableListOf<String>()
    private val listGruop2 = mutableListOf<String>()
    private val listGruop3 = mutableListOf<String>()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_main)

        groupname1.text = App.prefs.group1.toString()
        groupname2.text = App.prefs.group2.toString()
        groupname3.text = App.prefs.group3.toString()

        val listener = RadioListener()
        groupRadio.setOnCheckedChangeListener(listener)

        if(intent.hasExtra("name")){
            Log.d("HAVE NAME : ", intent.getStringExtra("name").toString())
            companyname = intent.getStringExtra("name")
            company_name.text = companyname
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

                        dataMax = dataList.maxOrNull()
                        dataMin = dataList.minOrNull()
                        Log.e("data max : ", dataMax.toString())
                        Log.e("data min : ", dataMin.toString())

                        dataFirst = dataList[0].toInt().toString()
                        Log.e("dataFirst : ", dataFirst.toString())
                    }

                    company_rate.text = dec.format(today) + "원"
                    Fluctuation_rate.text = percent1.toString() + "%"

                    one_max.text = dec.format(dataMax) + "원"
                    one_min.text = dec.format(dataMin) + "원"

                    capvalue.text = dec.format(cap) + "원"
                    PER.text = dec.format(per)
                    PBR.text = dec.format(pbr)

                    trading_volume.text = dec.format(volume) + "주"

                        isrunningYear = true
                        val thread = ThreadClass()
                        thread.start()


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

        AddCompany.setOnClickListener {
            addDataList(groupId!!)

            // val intent = Intent(this, MyFragment::class.java)

            if (groupId!! == 10){
                updateData(groupId!!.toInt(), App.prefs.id.toString(), groupName.toString(), App.prefs.getArrayList1()[0],App.prefs.getArrayList1()[1], App.prefs.getArrayList1()[2],
                        App.prefs.getArrayList1()[3], App.prefs.getArrayList1()[4], App.prefs.getArrayList1()[5], App.prefs.getArrayList1()[6], App.prefs.getArrayList1()[7],
                        App.prefs.getArrayList1()[8], App.prefs.getArrayList1()[9])
            } else if(groupId!! == 11) {
                updateData(groupId!!.toInt(), App.prefs.id.toString(), groupName.toString(), App.prefs.getArrayList2()[0], App.prefs.getArrayList2()[1], App.prefs.getArrayList2()[2],
                        App.prefs.getArrayList2()[3], App.prefs.getArrayList2()[4], App.prefs.getArrayList2()[5], App.prefs.getArrayList2()[6], App.prefs.getArrayList2()[7],
                        App.prefs.getArrayList2()[8], App.prefs.getArrayList2()[9])
            } else {
                updateData(groupId!!.toInt(), App.prefs.id.toString(), groupName.toString(), App.prefs.getArrayList3()[0], App.prefs.getArrayList3()[1], App.prefs.getArrayList3()[2],
                        App.prefs.getArrayList3()[3], App.prefs.getArrayList3()[4], App.prefs.getArrayList3()[5], App.prefs.getArrayList3()[6], App.prefs.getArrayList3()[7],
                        App.prefs.getArrayList3()[8], App.prefs.getArrayList3()[9])
            }

            // startActivity(intent)
        }

        codeaddbtn.setOnClickListener {
            App.prefs.code = code
            Log.d("CODE 추가 : " , App.prefs.code!!)
        }
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

    // dataListMonth
    inner class MyXAxisFormatter : ValueFormatter(){

        // private val days = arrayOf("1차","2차","3차","4차","5차","6차","7차")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return date_list.getOrNull(value.toInt()) ?: value.toString()
        }
    }

    inner class RadioListener : RadioGroup.OnCheckedChangeListener {
        @SuppressLint("SetTextI18n")
        override fun onCheckedChanged(p0: RadioGroup?, p1: Int) { // p1 사용자가 선택한 라디오 버튼의 아이디값
            when (p0?.id) {
                R.id.groupRadio ->
                    when (p1) {
                        R.id.groupname1 -> {
                            groupId = App.prefs.idGroup1?.toInt()
                            groupName = App.prefs.group1
                            Log.e("groupId : ", groupId.toString())
                            Log.e("groupName : ", groupName.toString())
                        }
                        R.id.groupname2 -> {
                            groupId = App.prefs.idGroup2?.toInt()
                            groupName = App.prefs.group2
                            Log.e("groupId : ", groupId.toString())
                            Log.e("groupName : ", groupName.toString())
                        }
                        R.id.groupname3 -> {
                            groupId = App.prefs.idGroup3?.toInt()
                            groupName = App.prefs.group3
                            Log.e("groupId : ", groupId.toString())
                            Log.e("groupName : ", groupName.toString())
                        }
                    }
            }
        }
    }

    private fun addDataList(groupId : Int){

        if(groupId == 10){
            listGruop1.clear()
            for(i in 0 until App.prefs.getArrayList1().count()){
                listGruop1.add(App.prefs.getArrayList1()[i])
                Log.d("getArrayList1 : " , App.prefs.getArrayList1()[i])
            }
            for(i in 0 until App.prefs.getArrayList1().count()){
                Log.d("listGruop1", listGruop1[i])
                Log.d("itemspos", companyname.toString())
                if(listGruop1[i] != companyname && listGruop1[i] == "null"){
                    listGruop1[i] = companyname.toString()
                    break
                }
            }
            App.prefs.saveArrayList1(listGruop1)

        } else if(groupId == 11){
            listGruop2.clear()
            for(i in 0 until App.prefs.getArrayList2().count()){
                listGruop2.add(App.prefs.getArrayList2()[i])
                Log.d("getArrayList2 : " , App.prefs.getArrayList2()[i])
            }
            for(i in 0 until App.prefs.getArrayList2().count()){
                Log.d("listGruop1", listGruop2[i])
                Log.d("itemspos", companyname.toString())
                if(listGruop2[i] != companyname && listGruop2[i] == "null"){
                    listGruop2[i] = companyname.toString()
                    break
                }
            }
            App.prefs.saveArrayList2(listGruop2)
        }
        else if(groupId == 12){
            listGruop3.clear()
            for(i in 0 until App.prefs.getArrayList3().count()){
                listGruop3.add(App.prefs.getArrayList3()[i])
                Log.d("getArrayList3 : " , App.prefs.getArrayList3()[i])
            }
            for(i in 0 until App.prefs.getArrayList3().count()){
                Log.d("listGruop1", listGruop3[i])
                Log.d("itemspos", companyname.toString())
                if(listGruop3[i] != companyname && listGruop3[i] == "null"){
                    listGruop3[i] = companyname.toString()
                    break
                }
            }
            App.prefs.saveArrayList3(listGruop3)
        }
    }

    private fun updateData(id : Int, name : String, group : String, company1 : String, company2 : String, company3 : String, company4 : String, company5 : String,
                           company6 : String, company7 : String, company8 : String, company9 : String, company10 : String,) {
        val companyService : UpdateService = retrofit.create(UpdateService::class.java)

        companyService.updateCompany(id, name, group, company1, company2, company3,company4, company5,company6,company7,
                company8, company9, company10).enqueue(object : Callback<UpdateData>{
            override fun onResponse(call: Call<UpdateData>, response: Response<UpdateData>) {
                val removeData = response.body()

                Log.d("response : " , removeData.toString())
            }

            override fun onFailure(call: Call<UpdateData>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}