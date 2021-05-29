package com.example.antenna.interest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.R
import com.example.antenna.adpater.InterAdapter
import kotlinx.android.synthetic.main.company_main.*

class InterCompany : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_main)

        if(intent.hasExtra("name")){
            Log.d("HAVE NAME : ", intent.getStringExtra("name").toString())
            company_name.text = intent.getStringExtra("name")
        } else{
            Log.d("HAVEN NAME : ", intent.getStringExtra("name").toString())
        }
    }
}