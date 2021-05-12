package com.example.antenna.dataclass

// Output data 생성 (응답값)
// 서버에서 가져올 값 추가 하기
data class FirstData(var body:FirstDataBody)
data class FirstDataBody(var name:String, var address:String, var phone_number:String)