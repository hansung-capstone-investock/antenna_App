package com.example.antenna.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.R
import com.example.antenna.`interface`.SignUpService
import com.example.antenna.dataclass.SignData
import com.example.antenna.fragment.InfoFragment
import kotlinx.android.synthetic.main.sign_up_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignActivity : AppCompatActivity() {

    var sign:SignData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page)

        // 입력 받은 ID PW
        val id = sign_id.text.toString()
        val pw = sign_password.text.toString()

        // val intent = Intent(this, InfoFragment::class.java)

        val dialog = AlertDialog.Builder(this@SignActivity)

        val signRetrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-13-125-236-101.ap-northeast-2.compute.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val signServer: SignUpService? = signRetrofit.create(SignUpService::class.java)


        signup_button.setOnClickListener {
            signServer?.requestsignup(id, pw)?.enqueue(object : Callback<SignData> {
                override fun onResponse(call: Call<SignData>, response: Response<SignData>) {
                    sign = response.body()
                    Log.d("RESPONSE MEG", response.toString())
                    Log.d("MESSAGE ", sign.toString())

                    // startActivity(intent)
                }

                override fun onFailure(call: Call<SignData>, t: Throwable) {
                    t.message?.let { it1 -> Log.e("LOGIN", it1) }
                    Log.e(signServer.toString(), "FAILLLL")
                    dialog.setTitle("실패!")
                    dialog.setMessage("통신에 실패하였습니다")
                    dialog.show()
                }

            })
        }
    }
}