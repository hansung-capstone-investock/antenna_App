package com.example.antenna.sharedPreference

import android.app.Application

class App : Application() {

    init {
        INSTANCE = this
    }

    companion object{
        lateinit var INSTANCE: App
        lateinit var prefs : SharedPreferences
    }

    override fun onCreate() {
        prefs = SharedPreferences(applicationContext)
        super.onCreate()
    }
}