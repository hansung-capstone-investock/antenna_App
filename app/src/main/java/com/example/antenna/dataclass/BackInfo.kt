package com.example.antenna.dataclass

data class BackInfo (
        var start: String,
        var end: String,
        var market: List<Int>,
        var sector: List<Int>,
        var perconditions: ArrayList<Int>? = null,
        var pbrconditions: ArrayList<Int>? = null,
        var psrconditions: ArrayList<Int>? = null,
        var roaconditions: ArrayList<Int>? = null,
        var roeconditions: ArrayList<Int>? = null,
        var sellCondition: List<Int>
)