package com.example.fingerprint_demo

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
    }

    companion object{
        var context : Context by Delegates.notNull()
        lateinit var instance : MyApplication
            private set
    }
}