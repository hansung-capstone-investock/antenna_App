package com.example.antenna.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.antenna.AntennaResult
import com.example.antenna.BackResult
import com.example.antenna.R
import com.example.antenna.`interface`.AntennaService
import com.example.antenna.`interface`.CompareService
import com.example.antenna.adpater.CommuityAdapter
import com.example.antenna.adpater.CommunityList
import com.example.antenna.adpater.CompareAdapter
import com.example.antenna.dataclass.AntennaData
import com.example.antenna.dataclass.CompareData
import com.example.antenna.interest.InterestActivity
import com.example.antenna.sharedPreference.App
import kotlinx.android.synthetic.main.fragment_antenna.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Response.success
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList


class AntennaFragment : Fragment() {

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
            .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
            .build()
    // 종목 코드
    var code : String? = null
    // 보조지표 리스트
    val subList : ArrayList<String> = arrayListOf()
    // 선택된 날짜
    var predictDate : Int? = null

    // 기업 리스트
    val companyList = mutableListOf<CommunityList>()
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

        antennabutton.setOnClickListener {
            antenna(code!!, subList, predictDate!!.toInt())
            /*val intentBack = Intent(context, AntennaResult::class.java)
            startActivity(intentBack)*/
        }

        compare()


        super.onViewCreated(view, savedInstanceState)
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
                    subList.add(p0.text as String)
                    Log.d("ADD : " , subList.toString())
                } else{
                    println("LIST $subList")
                }
            else {
                subList.remove(p0?.text as String)
                Log.d("remove : " , subList.toString())
            }
        }
    }

    fun antenna(code : String, indicator : ArrayList<String>, predictDate : Int){
        val AntennaService : AntennaService = retrofit.create(AntennaService::class.java)
        var antennaData : AntennaData? = null
        AntennaService.requestAntenna(code, indicator, predictDate).enqueue(object : Callback<AntennaData>{
            override fun onResponse(call: Call<AntennaData>, response: Response<AntennaData>) {

                val data = response.body()
                data?.let { success(data) }
                Log.e("data : ", data.toString())
                /*runBlocking {
                    delay(20000L)
                }*/
                antennaData = response.body()
                if(response.isSuccessful){
                    Log.e("antennaData : ", antennaData.toString())
                } else {
                    Log.e("antennaData : ", response.errorBody().toString())
                    Log.e("antennaData : ", response.code().toString())
                }
            }

            override fun onFailure(call: Call<AntennaData>, t: Throwable) {
                t.printStackTrace()
            }
        })
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
        selectcode.text = App.prefs.code
        code = selectcode.text as String?
        super.onResume()
    }
}
