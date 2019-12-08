package com.example.fingerprint_demo.fingerprint

import android.content.Context
import android.graphics.Color
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.example.fingerprint_demo.widget.FingerprintVerifyDialog

/**
 * @description 指纹验证扩展类
 */
class FingerprintImpl {

    private lateinit var context: Context
    private lateinit var fingerprintCallback: FingerprintCallback
    lateinit var fingerprintVerifyDialog: FingerprintVerifyDialog
    private lateinit var cancellationSignal : CancellationSignal
    private var VEFIRYMODE = 1

    companion object{

        const val PASS_VEFIRY = 0x0002

        private lateinit var cryptoObject : FingerprintManagerCompat.CryptoObject

        val instance by lazy {
            try {
                cryptoObject = FingerprintManagerCompat.CryptoObject(CipherHelper().createCipher(true))
            }catch (e : Exception){
                e.printStackTrace()
            }
            FingerprintImpl()
        }
    }

    /**
     * @description 初始化并调用指纹验证
     */
    fun authenticate(context : Context, callback: FingerprintCallback, flag : Int){
        this.context = context
        this.fingerprintCallback = callback
        cancellationSignal = CancellationSignal()
        fingerprintVerifyDialog = FingerprintVerifyDialog(context).setOnClickListener(onClickListener)
        cancellationSignal.setOnCancelListener { fingerprintVerifyDialog.dismiss() }
        VEFIRYMODE = flag
        FingerprintManagerCompat.from(context).authenticate(cryptoObject,0,cancellationSignal,authenticationCallback,null)
        fingerprintVerifyDialog.show()
    }

    /**
     * @description 验证框监听
     */
    private val onClickListener : FingerprintVerifyDialog.OnClickListener = object : FingerprintVerifyDialog.OnClickListener {

        override fun usePass() {
            fingerprintCallback.onCancel()
        }

        override fun onCancel() {
            fingerprintCallback.onCancel()
        }

        override fun onDismiss() {
            cancellationSignal.cancel()
        }
    }

    /**
     * @description 验证结果回调
     */
    private val authenticationCallback = object  : FingerprintManagerCompat.AuthenticationCallback(){

        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
            super.onAuthenticationError(errMsgId, errString)
            if(errMsgId != 5) fingerprintVerifyDialog.setTipText(errString!!.toString(), Color.RED)
        }

        override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            fingerprintVerifyDialog.setTipText("验证成功", Color.GREEN)
            fingerprintVerifyDialog.dismiss()
            fingerprintCallback.onSucceeded()

        }

        override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
            super.onAuthenticationHelp(helpMsgId, helpString)
            fingerprintVerifyDialog.setTipText(helpString!!.toString(), Color.YELLOW)
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            fingerprintVerifyDialog.setTipText("验证失败", Color.RED)
            if(VEFIRYMODE == PASS_VEFIRY){
                fingerprintVerifyDialog.openPassLogin()
            }
            fingerprintCallback.onFailed()
        }
    }

    /**
     * @description 判断当前是否支持指纹识别
     */
    fun canAuthenticate(context: Context, fingerprintCallback: FingerprintCallback) : Boolean{

        /**
         * @description 是否支持指纹识别
         */
        if(!FingerprintManagerCompat.from(context).isHardwareDetected){
            fingerprintCallback.onHwUnavailable()
            return false
        }
        /**
         * @description 是否已经添加指纹
         */
        if(!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()){
            fingerprintCallback.onNoneEnrolled()
            return false
        }
        return true
    }
}