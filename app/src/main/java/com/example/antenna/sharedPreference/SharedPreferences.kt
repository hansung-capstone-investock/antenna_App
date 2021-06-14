package com.example.antenna.sharedPreference

import android.content.Context
import android.content.SharedPreferences
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
        val json: String? = prefs.getString("save1", null)
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveArrayList2(list: MutableList<String>) {
        val json = gson.toJson(list)
        editor.putString("save2", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList2(): MutableList<String> {
        val json: String? = prefs.getString("save2", null)
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveArrayList3(list: MutableList<String>) {
        val json = gson.toJson(list)
        editor.putString("save3", json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList3(): MutableList<String> {
        val json: String? = prefs.getString("save3", null)
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type)
    }
}