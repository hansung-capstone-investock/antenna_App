package com.example.antenna.sharedPreference

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    private val prefs : SharedPreferences =
            context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    var editor: SharedPreferences.Editor = prefs.edit()

    var id: String?
        get() = prefs.getString("ID", "")
        set(value) = prefs.edit().putString("ID", value).apply()
}