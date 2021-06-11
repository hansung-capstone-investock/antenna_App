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

    private val list = mutableListOf<DataList>()
    private val adapter1 = RecyclerAdapter(list)

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

            Log.d("LOG NAME", App.prefs.id.toString())
//            Log.d("getArrayList1 DELETE", App.prefs.getArrayList1().toString())

            (context as Activity).finish()
        }
        mypage_user.text = App.prefs.id.toString() + "님 안녕하세요"

        inter_name1.text = App.prefs.getArrayList1().group as CharSequence?
        inter_name2.text = App.prefs.getArrayList2()?.group as CharSequence?
        inter_name3.text = App.prefs.getArrayList3()?.group as CharSequence?

        // 관심종목 리스트 추가
        rv_data_mypage.adapter = adapter1
    }

    override fun onResume() {
        super.onResume()

    }
}