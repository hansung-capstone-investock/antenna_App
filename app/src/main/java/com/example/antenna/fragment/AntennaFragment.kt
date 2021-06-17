package com.example.antenna.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.antenna.AntennaResult
import com.example.antenna.R
import com.example.antenna.`interface`.AntennaService
import com.example.antenna.`interface`.CompareService
import com.example.antenna.adpater.CommunityList
import com.example.antenna.adpater.CompareAdapter
import com.example.antenna.dataclass.AntennaData
import com.example.antenna.dataclass.AtennaInfo
import com.example.antenna.dataclass.CompareData
import com.example.antenna.interest.InterestActivity
import com.example.antenna.sharedPreference.App
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_antenna.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Response.success
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class AntennaFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job // 2
    override val coroutineContext: CoroutineContext // 3
        get() = Dispatchers.Main + job


    private val client : OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    val request : Request = Request.Builder()
            .url("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/")
            .addHeader("Connection", "close")
            .get()
            .build()

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
            .build()

    // 종목 코드
    var code : String? = null
    // 보조지표 리스트
    val indicator : ArrayList<String> = arrayListOf()
    // 선택된 날짜
    var predictDate : Int? = null

    // 기업 리스트
    val companyList = mutableListOf<CommunityList>()
    // predict
    private val predictList = ArrayList<Double>()
    // actual
    private val actualList = ArrayList<Double>()
    // 어뎁터
    private val adapter2 = CompareAdapter(companyList)

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_antenna, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val listener = RadioListener()
        val subListener = CheckBoxListener()

        dateGroup.setOnCheckedChangeListener(listener)

        VPTButton.setOnCheckedChangeListener(subListener)
        CMFradioButton.setOnCheckedChangeListener(subListener)
        EoMradioButton.setOnCheckedChangeListener(subListener)
        FIradioButton.setOnCheckedChangeListener(subListener)
        ADIradioButton.setOnCheckedChangeListener(subListener)
        VWAPradioButton.setOnCheckedChangeListener(subListener)

        DCradioButton.setOnCheckedChangeListener(subListener)
        KCradioButton.setOnCheckedChangeListener(subListener)
        PPOradioButton.setOnCheckedChangeListener(subListener)
        RSIradioButton.setOnCheckedChangeListener(subListener)
        SRSI.setOnCheckedChangeListener(subListener)
        TSIradioButton.setOnCheckedChangeListener(subListener)

        UOradioButton.setOnCheckedChangeListener(subListener)
        WRradioButton.setOnCheckedChangeListener(subListener)
        KAMAradioButton.setOnCheckedChangeListener(subListener)
        CCIradioButton.setOnCheckedChangeListener(subListener)
        DPOradioButton.setOnCheckedChangeListener(subListener)
        DIFFradioButton.setOnCheckedChangeListener(subListener)

        STCradioButton.setOnCheckedChangeListener(subListener)
        TRIXradioButton.setOnCheckedChangeListener(subListener)
        MACDradioButton.setOnCheckedChangeListener(subListener)
        WMAradioButton.setOnCheckedChangeListener(subListener)
        EMAradioButton.setOnCheckedChangeListener(subListener)
        KSTradioButton.setOnCheckedChangeListener(subListener)
        DIFFRATEradioButton.setOnCheckedChangeListener(subListener)

        searchbtn.setOnClickListener {
            val searchIntent = Intent(context, InterestActivity::class.java)

            startActivity(searchIntent)
        }

        compare()
        super.onViewCreated(view, savedInstanceState)
        job = Job()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    inner class Thraed : Thread(){

    }

    inner class RadioListener : RadioGroup.OnCheckedChangeListener {
        @SuppressLint("SetTextI18n")
        override fun onCheckedChanged(p0: RadioGroup?, p1: Int) { // p1 사용자가 선택한 라디오 버튼의 아이디값
            when (p0?.id) {
                R.id.dateGroup ->
                    when (p1) {
                        R.id.radioButton7 -> {
                            textday.text = "7"
                            predictDate = 7
                            Log.d("predictDate : ", predictDate.toString())
                        }
                        R.id.radioButton14 -> {
                            textday.text = "14"
                            predictDate = 14
                            Log.d("predictDate : ", predictDate.toString())
                        }
                            R.id.radioButton21 -> {
                            textday.text = "21"
                            predictDate = 21
                            Log.d("predictDate : ", predictDate.toString())
                        }
                        R.id.radioButton28 -> {
                            textday.text = "28"
                            predictDate = 28
                            Log.d("predictDate : ", predictDate.toString())
                        }
                    }
            }
        }
    }

    inner class CheckBoxListener : CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            if(p1)
                if (p0 != null) {
                    indicator.add(p0.text as String)
                    Log.d("ADD : " , indicator.toString())
                } else{
                    println("LIST $indicator")
                }
            else {
                indicator.remove(p0?.text as String)
                Log.d("remove : " , indicator.toString())
            }
        }
    }

    fun antenna(code : String, indicator : List<String>, predictDate : Int){

        val antennaService : AntennaService = retrofit.create(AntennaService::class.java)
        val intentBack = Intent(context, AntennaResult::class.java)

        launch {
            antennaService.requestAntenna(AtennaInfo(code, indicator, predictDate)).enqueue(object : Callback<AntennaData> {
                override fun onFailure(call: Call<AntennaData>, t: Throwable) {
                    Log.e("ERROR : ", t.toString())
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<AntennaData>, response: Response<AntennaData>) {
                    val data = response.body()
                    data?.let { success(data) }

                    if (response.isSuccessful) {
                        /*Log.e("antennaData : predict", data?.predict.toString())
                        Log.e("antennaData : actual ", data?.actual.toString())*/
                        Log.e("data :  ", data!!.toString())

                        predictList.add(data.predict.day1)
                        predictList.add(data.predict.day2)
                        predictList.add(data.predict.day3)
                        predictList.add(data.predict.day4)
                        predictList.add(data.predict.day5)
                        predictList.add(data.predict.day6)
                        predictList.add(data.predict.day7)
                        predictList.add(data.predict.day8)
                        predictList.add(data.predict.day9)
                        predictList.add(data.predict.day10)
                        predictList.add(data.predict.day11)
                        predictList.add(data.predict.day12)
                        predictList.add(data.predict.day13)
                        predictList.add(data.predict.day14)
                        predictList.add(data.predict.day15)
                        predictList.add(data.predict.day16)
                        predictList.add(data.predict.day17)
                        predictList.add(data.predict.day18)
                        predictList.add(data.predict.day19)
                        predictList.add(data.predict.day20)
                        predictList.add(data.predict.day21)
                        predictList.add(data.predict.day22)
                        predictList.add(data.predict.day23)
                        predictList.add(data.predict.day24)
                        predictList.add(data.predict.day25)
                        predictList.add(data.predict.day26)
                        predictList.add(data.predict.day27)
                        predictList.add(data.predict.day28)
                        predictList.add(data.predict.day29)
                        predictList.add(data.predict.day30)
                        predictList.add(data.predict.day31)
                        predictList.add(data.predict.day32)
                        predictList.add(data.predict.day33)
                        predictList.add(data.predict.day34)
                        predictList.add(data.predict.day35)
                        predictList.add(data.predict.day36)
                        predictList.add(data.predict.day37)
                        predictList.add(data.predict.day38)
                        predictList.add(data.predict.day39)
                        predictList.add(data.predict.day40)
                        predictList.add(data.predict.day41)
                        predictList.add(data.predict.day42)
                        predictList.add(data.predict.day43)
                        predictList.add(data.predict.day44)
                        predictList.add(data.predict.day45)
                        predictList.add(data.predict.day46)
                        predictList.add(data.predict.day47)
                        predictList.add(data.predict.day48)
                        predictList.add(data.predict.day49)
                        predictList.add(data.predict.day50)
                        predictList.add(data.predict.day51)
                        predictList.add(data.predict.day52)
                        predictList.add(data.predict.day53)
                        predictList.add(data.predict.day54)
                        predictList.add(data.predict.day55)
                        predictList.add(data.predict.day56)
                        predictList.add(data.predict.day57)
                        predictList.add(data.predict.day58)
                        predictList.add(data.predict.day59)
                        predictList.add(data.predict.day60)
                        predictList.add(data.predict.day61)
                        predictList.add(data.predict.day62)
                        predictList.add(data.predict.day63)
                        predictList.add(data.predict.day64)
                        predictList.add(data.predict.day65)
                        predictList.add(data.predict.day66)
                        predictList.add(data.predict.day67)
                        predictList.add(data.predict.day68)
                        predictList.add(data.predict.day69)
                        predictList.add(data.predict.day70)
                        predictList.add(data.predict.day71)
                        predictList.add(data.predict.day72)
                        predictList.add(data.predict.day73)
                        predictList.add(data.predict.day74)
                        predictList.add(data.predict.day75)
                        predictList.add(data.predict.day76)
                        predictList.add(data.predict.day77)
                        predictList.add(data.predict.day78)
                        predictList.add(data.predict.day79)
                        predictList.add(data.predict.day80)
                        predictList.add(data.predict.day81)
                        predictList.add(data.predict.day82)
                        predictList.add(data.predict.day83)
                        predictList.add(data.predict.day84)
                        predictList.add(data.predict.day85)
                        predictList.add(data.predict.day86)
                        predictList.add(data.predict.day87)
                        predictList.add(data.predict.day88)
                        predictList.add(data.predict.day89)
                        predictList.add(data.predict.day90)
                        predictList.add(data.predict.day91)
                        predictList.add(data.predict.day92)
                        predictList.add(data.predict.day93)
                        predictList.add(data.predict.day94)
                        predictList.add(data.predict.day95)
                        predictList.add(data.predict.day96)
                        predictList.add(data.predict.day97)
                        predictList.add(data.predict.day98)
                        predictList.add(data.predict.day99)
                        predictList.add(data.predict.day100)
                        predictList.add(data.predict.day101)
                        predictList.add(data.predict.day102)
                        predictList.add(data.predict.day103)
                        predictList.add(data.predict.day104)
                        predictList.add(data.predict.day105)

                        actualList.add(data.actual.day1)
                        actualList.add(data.actual.day2)
                        actualList.add(data.actual.day3)
                        actualList.add(data.actual.day4)
                        actualList.add(data.actual.day5)
                        actualList.add(data.actual.day6)
                        actualList.add(data.actual.day7)
                        actualList.add(data.actual.day8)
                        actualList.add(data.actual.day9)
                        actualList.add(data.actual.day10)
                        actualList.add(data.actual.day11)
                        actualList.add(data.actual.day12)
                        actualList.add(data.actual.day13)
                        actualList.add(data.actual.day14)
                        actualList.add(data.actual.day15)
                        actualList.add(data.actual.day16)
                        actualList.add(data.actual.day17)
                        actualList.add(data.actual.day18)
                        actualList.add(data.actual.day19)
                        actualList.add(data.actual.day20)
                        actualList.add(data.actual.day21)
                        actualList.add(data.actual.day22)
                        actualList.add(data.actual.day23)
                        actualList.add(data.actual.day24)
                        actualList.add(data.actual.day25)
                        actualList.add(data.actual.day26)
                        actualList.add(data.actual.day27)
                        actualList.add(data.actual.day28)
                        actualList.add(data.actual.day29)
                        actualList.add(data.actual.day30)
                        actualList.add(data.actual.day31)
                        actualList.add(data.actual.day32)
                        actualList.add(data.actual.day33)
                        actualList.add(data.actual.day34)
                        actualList.add(data.actual.day35)
                        actualList.add(data.actual.day36)
                        actualList.add(data.actual.day37)
                        actualList.add(data.actual.day38)
                        actualList.add(data.actual.day39)
                        actualList.add(data.actual.day40)
                        actualList.add(data.actual.day41)
                        actualList.add(data.actual.day42)
                        actualList.add(data.actual.day43)
                        actualList.add(data.actual.day44)
                        actualList.add(data.actual.day45)
                        actualList.add(data.actual.day46)
                        actualList.add(data.actual.day47)
                        actualList.add(data.actual.day48)
                        actualList.add(data.actual.day49)
                        actualList.add(data.actual.day50)
                        actualList.add(data.actual.day51)
                        actualList.add(data.actual.day52)
                        actualList.add(data.actual.day53)
                        actualList.add(data.actual.day54)
                        actualList.add(data.actual.day55)
                        actualList.add(data.actual.day56)
                        actualList.add(data.actual.day57)
                        actualList.add(data.actual.day58)
                        actualList.add(data.actual.day59)
                        actualList.add(data.actual.day60)
                        actualList.add(data.actual.day61)
                        actualList.add(data.actual.day62)
                        actualList.add(data.actual.day63)
                        actualList.add(data.actual.day64)
                        actualList.add(data.actual.day65)
                        actualList.add(data.actual.day66)
                        actualList.add(data.actual.day67)
                        actualList.add(data.actual.day68)
                        actualList.add(data.actual.day69)
                        actualList.add(data.actual.day70)
                        actualList.add(data.actual.day71)
                        actualList.add(data.actual.day72)
                        actualList.add(data.actual.day73)
                        actualList.add(data.actual.day74)
                        actualList.add(data.actual.day75)
                        actualList.add(data.actual.day76)
                        actualList.add(data.actual.day77)
                        actualList.add(data.actual.day78)
                        actualList.add(data.actual.day79)
                        actualList.add(data.actual.day80)
                        actualList.add(data.actual.day81)
                        actualList.add(data.actual.day82)
                        actualList.add(data.actual.day83)
                        actualList.add(data.actual.day84)
                        actualList.add(data.actual.day85)
                        actualList.add(data.actual.day86)
                        actualList.add(data.actual.day87)
                        actualList.add(data.actual.day88)
                        actualList.add(data.actual.day89)
                        actualList.add(data.actual.day90)
                        actualList.add(data.actual.day91)
                        actualList.add(data.actual.day92)
                        actualList.add(data.actual.day93)
                        actualList.add(data.actual.day94)
                        actualList.add(data.actual.day95)
                        actualList.add(data.actual.day96)
                        actualList.add(data.actual.day97)
                        actualList.add(data.actual.day98)

                        App.prefs.predictAntennaListSV(predictList)
                        App.prefs.actualAntennaListSV(actualList)
                        startActivity(intentBack)

                    } else {
                        Log.e("code : ", response.code().toString())
                        try {
                            Log.v("FAILL :", response.body().toString())
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            })
        }
    }

    private fun compare(){
        val compareService : CompareService? = retrofit.create(CompareService::class.java)

        var compareData : CompareData? = null

        compareService?.getCompare()?.enqueue(object : Callback<CompareData>{
            override fun onResponse(call: Call<CompareData>, response: Response<CompareData>) {
                compareData = response.body()

                for(i in 0 until 5){
                    val name = compareData?.elementAt(i)?.company
                    val percent = compareData?.elementAt(i)?.gap
                    companyList.add(CommunityList(i+1, name.toString(), percent.toString()))
                }
                rv_data_compare.adapter = adapter2
            }

            override fun onFailure(call: Call<CompareData>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onResume() {

        selectname.text = App.prefs.codeName
        selectcode.text = App.prefs.code

        antennabutton.setOnClickListener {
            code = selectcode.text as String?
            Log.e("code : " , code.toString())
            Log.e("indicator : " , indicator.toString())
            Log.e("predictDate : " , predictDate.toString())

            code?.let { it1 -> antenna(it1, indicator, predictDate!!) }

        }

        super.onResume()
    }
}
