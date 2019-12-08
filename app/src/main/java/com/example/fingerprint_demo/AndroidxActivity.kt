package com.example.fingerprint_demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_androidx.*
import java.util.concurrent.Executor

class AndroidxActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_androidx)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,callback)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Title")
            .setSubtitle("SubTitle")
            .setDescription("放置您的手指")
            .setNegativeButtonText("使用密码登录")
            .build()
        use_andoridx_text.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            Toast.makeText(applicationContext, "验证错误: $errString", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Toast.makeText(applicationContext, "验证成功", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@AndroidxActivity,MainActivity::class.java))
            finish()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(applicationContext, "验证失败", Toast.LENGTH_SHORT).show()
        }
    }
}
