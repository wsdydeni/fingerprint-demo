package com.example.fingerprint_demo.util

import android.content.Context
import android.content.SharedPreferences
import com.example.fingerprint_demo.MyApplication
import kotlin.reflect.KProperty

class MyPreference<T>(private val name : String, private val default : T) {

    private val sharedPreferences : SharedPreferences
            by lazy { MyApplication.instance.applicationContext.getSharedPreferences(
                Constants.SHARE_PREFERENCE_NAME,Context.MODE_PRIVATE) }

    operator fun getValue(thisRef : Any?,property : KProperty<*>) : T = getSharedPreferences(name,default)

    operator fun setValue(thisRef: Any?,property: KProperty<*>,value: T) = putSharedPreferences(name,value)


    private fun putSharedPreferences(name : String,value : T)  = with(sharedPreferences.edit()){
        when(value){
            is Int -> putInt(name,value)
            is String -> putString(name,value)
            is Float -> putFloat(name,value)
            is Long -> putLong(name,value)
            is Boolean -> putBoolean(name,value)
            else -> throw IllegalArgumentException("SharedPreference can't be save this type")
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSharedPreferences(name : String, default: T) : T = with(sharedPreferences){
        val res : Any = when(default){
            is Int -> getInt(name,default)
            is Boolean -> getBoolean(name,default)
            is String -> getString(name,default)!!
            is Float -> getFloat(name,default)
            is Long -> getLong(name,default)
            else -> throw IllegalArgumentException("SharedPreference can't be get this type")
        }
        return res as T
    }

}

object DelegatesExt {
    fun <T> myPreferences(name : String,default : T) =
        MyPreference(name, default)
}

class Constants{
    companion object{
        const val SHARE_PREFERENCE_NAME = "MySharePreference"
        const val OPEN_FINGERPRINT_LOGIN = "open_fingerprint_login"
        const val IS_LOGIN = "is_login"
    }
}