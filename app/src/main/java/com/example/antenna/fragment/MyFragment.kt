package com.example.antenna.fragment

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
import com.example.antenna.sharedPreference.App
import kotlinx.android.synthetic.main.fragment_mypage.*

class MyFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 회원정보 삭제
        Logout.setOnClickListener {
            App.prefs.editor.clear()
            App.prefs.editor.commit()

            Log.d("LOG NAME", App.prefs.id.toString())
            Log.d("getArrayList1 DELETE", App.prefs.getArrayList1().toString())

            (context as Activity).finish()

            // 다시하기
//            activity.let{
//                val intent = Intent(it, InfoFragment()::class.java)
//                it?.startActivity(intent)
//                //activity?.finish()
//            }
        }
    }



    override fun onResume() {
        super.onResume()

    }
}