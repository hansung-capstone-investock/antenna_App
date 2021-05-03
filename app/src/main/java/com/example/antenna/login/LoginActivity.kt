package com.example.antenna.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.antenna.R
import kotlinx.android.synthetic.main.login_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {
    var login:Login? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        var retrofit = Retrofit.Builder()
                .baseUrl("http://926fe53947d9.ngrok.io/") // 장고 서버 주소 입력
                .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
                .build()

        var loginService : LoginService = retrofit.create(LoginService::class.java)

        button2.setOnClickListener {
            Log.d(loginService.toString(), "button!!!!!!!!!!!!!!!!!")
            // 입력된 ID, PW 값 가져오기
            // 로그인 성공시 이 값을 넘겨주기
            var id = userid.text.toString()
            var pw = userpw.text.toString()

            // 웹 통신를 할떄 별도의 다른 스레드에서 작업을 해야함(메인 액티비티 스레드가 아닌)

            loginService.requestLogin(id, pw).enqueue(object : Callback<Login>{

                // 웹 통신 실패했을 떄 실행
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    t.message?.let { it1 -> Log.e("LOGIN", it1) }
                    Log.d(loginService.toString(), "FA!!!!!!!!!!!!!!!!!")
                    dialog.setTitle("실패!")
                    dialog.setMessage("통신에 실패하였습니다")
                    dialog.show()
                }

                // 웹 통신 성공시 실행
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    login = response.body() // code, msg 값을 객체로 넣음
                    // output 정의를 가져옴
                    Log.d(response.toString(), "..ERROR")
                    Log.e("RES", response.message())

                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("성공")

                    dialog.setMessage("code = " + login?.code + "msg = " + login?.msg )
                    dialog.show()
                    
                    // 로그인 성공했을 경우 원래 액티비티로 돌아가기
                    // finish()
                }


            })
        }
    }
}
