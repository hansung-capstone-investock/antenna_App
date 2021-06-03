package com.example.antenna.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import com.example.antenna.dataclass.LoadData
import com.example.antenna.dataclass.LoadDataItem
import com.google.gson.Gson

class SharedPreferences(context: Context) {


    var loadData: ArrayList<LoadDataItem>
        get() = TODO()
        set(value) = prefs.edit().putString("load", value.toString()).apply()


    private val prefs : SharedPreferences =
            context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    var editor: SharedPreferences.Editor = prefs.edit()

    var id: String?
        get() = prefs.getString("ID", "")
        set(value) = prefs.edit().putString("ID", value).apply()

}