package com.example.antenna.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antenna.MainActivity
import com.example.antenna.R
import com.example.antenna.adpater.DataList
import com.example.antenna.adpater.RecyclerAdapter
import com.example.antenna.adpater.ViewPagerAdapter
import com.example.antenna.sharedPreference.App
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_mypage.*

class MyFragment : Fragment(){

    private val list1 = mutableListOf<DataList>()
    private val list2 = mutableListOf<DataList>()
    private val list3 = mutableListOf<DataList>()

    private val adapter1 = RecyclerAdapter(list1)
    private val adapter2 = RecyclerAdapter(list2)
    private val adapter3 = RecyclerAdapter(list3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 회원정보 삭제
        Logout.setOnClickListener {
            App.prefs.editor.clear()
            App.prefs.editor.commit()

            (context as Activity).finish()
        }
        mypage_user.text = App.prefs.id.toString() + "님 안녕하세요"

        inter_name1.text = App.prefs.group1
        inter_name2.text = App.prefs.group2
        inter_name3.text = App.prefs.group3

        // 관심종목 리스트 추가
        rv_data_mypage.adapter = adapter1
    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {

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
            rv_data_mypage.adapter = adapter1
        }

        inter_name2.setOnClickListener {
            rv_data_mypage.adapter = adapter2
        }

        inter_name3.setOnClickListener {
            rv_data_mypage.adapter = adapter3
        }

        super.onResume()
    }
}