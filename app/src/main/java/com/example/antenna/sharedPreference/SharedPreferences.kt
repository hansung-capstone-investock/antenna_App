package com.example.antenna.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import com.example.antenna.dataclass.LoadData
import com.example.antenna.dataclass.LoadDataItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SharedPreferences(context: Context) {

    private val prefs : SharedPreferences =
            context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    var editor: SharedPreferences.Editor = prefs.edit()
    val gson = Gson()

    var id: String?
        get() = prefs.getString("ID", "")
        set(value) = prefs.edit().putString("ID", value).apply()

    fun saveArrayList1(list: LoadDataItem) {
        val json = gson.toJson(list)
        editor.putString("save1", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList1(): LoadDataItem {
        val json: String? = prefs.getString("save1", null)
        val type = object : TypeToken<LoadDataItem>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveArrayList2(list: LoadDataItem) {
        val json = gson.toJson(list)
        editor.putString("save2", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList2(): LoadDataItem? {
        val json: String? = prefs.getString("save2", null)
        val type = object : TypeToken<LoadDataItem>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveArrayList3(list: LoadDataItem) {
        val json = gson.toJson(list)
        editor.putString("save3", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList3(): LoadDataItem? {
        val json: String? = prefs.getString("save3", null)
        val type = object : TypeToken<LoadDataItem>() {}.type
        return gson.fromJson(json, type)
    }
}