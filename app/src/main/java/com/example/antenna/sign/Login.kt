package com.example.antenna.sign

// Output data 생성 (응답값)
// 서버에서 가져올 값 추가 하기
data class Login(
        var code : String,
        var msg : String
)
// Json에 있는 키 값과 같아야한다
// 다른 거 가져올 떄