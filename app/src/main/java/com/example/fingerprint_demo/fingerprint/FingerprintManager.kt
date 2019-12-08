package com.example.fingerprint_demo.fingerprint

import android.content.Context

class FingerprintManager(private var context: Context, private var callback: FingerprintCallback) {

    //是否支持指纹识别
    var isSupportFingerLogin : Boolean = false

    private var fingerprintImpl : FingerprintImpl = FingerprintImpl.instance

    init {
        isSupportFingerLogin = fingerprintImpl.canAuthenticate(context,callback)
    }

    /**
     * @description 开始指纹识别
     */
    fun autherticate(flag : Int){
        fingerprintImpl.authenticate(context,callback,flag)
    }
}