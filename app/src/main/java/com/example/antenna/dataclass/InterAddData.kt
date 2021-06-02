package com.example.antenna.dataclass

data class InterAddData(
        // 리턴 값 요청시 받아오는 값들
        var name : String,
        var group : String,
        var company : ArrayList<String>,
)