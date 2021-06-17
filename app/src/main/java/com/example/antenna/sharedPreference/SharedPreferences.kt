package com.example.antenna.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SharedPreferences(context: Context) {

    private val prefs : SharedPreferences =
            context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    var editor: SharedPreferences.Editor = prefs.edit()
    val gson = Gson()

    var idGroup1 : String?
        get() = prefs.getString("idGroup1", "")
        set(value) = prefs.edit().putString("idGroup1", value).apply()

    var idGroup2 : String?
        get() = prefs.getString("idGroup2", "")
        set(value) = prefs.edit().putString("idGroup2", value).apply()

    var idGroup3 : String?
        get() = prefs.getString("idGroup3", "")
        set(value) = prefs.edit().putString("idGroup3", value).apply()

    var id: String?
        get() = prefs.getString("ID", "")
        set(value) = prefs.edit().putString("ID", value).apply()

    // 종목명
    var codeName: String?
        get() = prefs.getString("codeName", "")
        set(value) = prefs.edit().putString("codeName", value).apply()

    var code: String?
        get() = prefs.getString("code", "")
        set(value) = prefs.edit().putString("code", value).apply()

    var group1: String?
        get() = prefs.getString("group1", "")
        set(value) = prefs.edit().putString("group1", value).apply()

    var group2: String?
        get() = prefs.getString("group2", "")
        set(value) = prefs.edit().putString("group2", value).apply()

    var group3: String?
        get() = prefs.getString("group3", "")
        set(value) = prefs.edit().putString("group3", value).apply()

    fun saveArrayList1(list: MutableList<String>) {
        val json = gson.toJson(list)
        editor.putString("save1", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList1(): MutableList<String> {
        val emptyList = Gson().toJson(ArrayList<String>())
        return Gson().fromJson(
                prefs.getString("save1", emptyList),
                object : TypeToken<MutableList<String>>(){
                }.type
        )
    }

    fun saveArrayList2(list: MutableList<String>) {
        val json = gson.toJson(list)
        editor.putString("save2", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList2(): MutableList<String> {
        val emptyList = Gson().toJson(ArrayList<String>())
        return Gson().fromJson(
                prefs.getString("save2", emptyList),
                object : TypeToken<MutableList<String>>(){
                }.type
        )
    }

    fun saveArrayList3(list: MutableList<String>) {
        val json = gson.toJson(list)
        editor.putString("save3", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList3(): MutableList<String> {
        val emptyList = Gson().toJson(ArrayList<String>())
        return Gson().fromJson(
                prefs.getString("save3", emptyList),
                object : TypeToken<MutableList<String>>(){
                }.type
        )
    }

    fun predictAntennaGET(): ArrayList<Double> {
        val emptyList = Gson().toJson(ArrayList<Double>())
        return Gson().fromJson(
                prefs.getString("predict", emptyList),
                object : TypeToken<ArrayList<Double>>(){
                }.type
        )
    }

    fun predictAntennaListSV(list: ArrayList<Double>) {
        val json = gson.toJson(list)
        editor.putString("predict", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun actualAntennaGET(): ArrayList<Double> {
        val emptyList = Gson().toJson(ArrayList<Double>())
        return Gson().fromJson(
                prefs.getString("actual", emptyList),
                object : TypeToken<ArrayList<Double>>(){
                }.type
        )
    }

    fun actualAntennaListSV(list: ArrayList<Double>) {
        val json = gson.toJson(list)
        editor.putString("actual", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun backGET(): ArrayList<Double> {
        val emptyList = Gson().toJson(ArrayList<Double>())
        return Gson().fromJson(
                prefs.getString("back", emptyList),
                object : TypeToken<ArrayList<Double>>(){
                }.type
        )
    }

    fun backSV(list: ArrayList<Any>) {
        val json = gson.toJson(list)
        editor.putString("back", json)
        editor.apply() // This line is IMPORTANT !!!
    }
}