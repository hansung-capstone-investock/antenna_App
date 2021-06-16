package com.example.antenna.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.antenna.MainActivity
import com.example.antenna.R
import com.example.antenna.`interface`.UpdateService
import com.example.antenna.adpater.*
import com.example.antenna.dataclass.UpdateData
import com.example.antenna.sharedPreference.App
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyFragment : Fragment(){

    private val list1 = mutableListOf<DataList>()
    private val list2 = mutableListOf<DataList>()
    private val list3 = mutableListOf<DataList>()

    private val adapter1 = RecyclerAdapter(list1)
    private val adapter2 = RecyclerAdapter1(list2)
    private val adapter3 = RecyclerAdapter2(list3)

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
            .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
            .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = this
        // 회원정보 삭제
        Logout.setOnClickListener {
            App.prefs.editor.clear()
            App.prefs.editor.commit()

            (context as Activity).finish()
        }
        mypage_user.text = App.prefs.id.toString() + "님 안녕하세요"

        // 관심종목 리스트 추가
        rv_data_mypage.adapter = adapter1

        interedit1.setOnClickListener {
            val builder : AlertDialog.Builder = android.app.AlertDialog.Builder(requireActivity())
            builder.setTitle("관심종목 이름 변경")

            val input = EditText(requireActivity())
            input.setHint("Enter Text")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, which ->
                // Here you get get input text from the Edittext
                var m_Text = input.text.toString()
                App.prefs.group1 = m_Text
                updateData(App.prefs.idGroup1!!.toInt(), App.prefs.id.toString(), App.prefs.group1.toString(), App.prefs.getArrayList1()[0],App.prefs.getArrayList1()[1], App.prefs.getArrayList1()[2],
                        App.prefs.getArrayList1()[3], App.prefs.getArrayList1()[4], App.prefs.getArrayList1()[5], App.prefs.getArrayList1()[6], App.prefs.getArrayList1()[7],
                        App.prefs.getArrayList1()[8], App.prefs.getArrayList1()[9])
            }
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {

        inter_name1.text = App.prefs.group1
        inter_name2.text = App.prefs.group2
        inter_name3.text = App.prefs.group3

        list1.clear()
        list2.clear()
        list3.clear()

        for(i in 0 until App.prefs.getArrayList1().count()){
            if(App.prefs.getArrayList1()[i] != "null") {
                list1.add(DataList(App.prefs.getArrayList1()[i]))
            }
        }

        for(i in 0 until App.prefs.getArrayList2().count()){
            if(App.prefs.getArrayList2()[i] != "null") {
                list2.add(DataList(App.prefs.getArrayList2()[i]))
            }
        }

        for(i in 0 until App.prefs.getArrayList3().count()){
            if(App.prefs.getArrayList3()[i] != "null") {
                list3.add(DataList(App.prefs.getArrayList3()[i]))
            }
        }

        inter_name1.setOnClickListener {
            inter_name1.setTextColor(R.color.black)
            inter_name2.setTextColor(R.color.white)
            inter_name3.setTextColor(R.color.white)
            rv_data_mypage.adapter = adapter1
        }

        inter_name2.setOnClickListener {
            inter_name1.setTextColor(R.color.white)
            inter_name2.setTextColor(R.color.black)
            inter_name3.setTextColor(R.color.white)
            rv_data_mypage.adapter = adapter2
        }

        inter_name3.setOnClickListener {
            inter_name1.setTextColor(R.color.white)
            inter_name2.setTextColor(R.color.white)
            inter_name3.setTextColor(R.color.black)
            rv_data_mypage.adapter = adapter3
        }

        super.onResume()
    }

    private fun updateData(id : Int, name : String, group : String, company1 : String, company2 : String, company3 : String, company4 : String, company5 : String,
                           company6 : String, company7 : String, company8 : String, company9 : String, company10 : String,) {
        val companyService : UpdateService = retrofit.create(UpdateService::class.java)

        companyService.updateCompany(id, name, group, company1, company2, company3,company4, company5,company6,company7,
                company8, company9, company10).enqueue(object : Callback<UpdateData> {
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