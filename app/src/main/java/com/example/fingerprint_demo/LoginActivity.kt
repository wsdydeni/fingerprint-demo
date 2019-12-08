package com.example.fingerprint_demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fingerprint_demo.fingerprint.FingerprintCallback
import com.example.fingerprint_demo.fingerprint.FingerprintManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object{  const val VEFIRY = 0x0002  }

    private lateinit var fingerprintManager: FingerprintManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        fingerprint_text.setOnClickListener {
            fingerprint_text.postDelayed({
                fingerprintManager = FingerprintManager(this,callback)
                if(fingerprintManager.isSupportFingerLogin) {
                    fingerprintManager.autherticate(VEFIRY)
                }
            },2000)
        }
        androidx_fingerprint_text.setOnClickListener {
            startActivity(Intent(this,AndroidxActivity::class.java))
            finish()
        }
    }


    private val callback = object : FingerprintCallback {

        override fun onCancel() {
            Toast.makeText(this@LoginActivity,"验证取消", Toast.LENGTH_SHORT).show()
        }

        override fun onFailed() {
            Toast.makeText(this@LoginActivity,"验证失败", Toast.LENGTH_SHORT).show()
        }

        override fun onSucceeded() {
            Toast.makeText(this@LoginActivity,"验证成功", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            this@LoginActivity.finish()
        }

        override fun onNoneEnrolled() {
            Toast.makeText(this@LoginActivity,"您还未添加指纹", Toast.LENGTH_SHORT).show()
        }

        override fun onHwUnavailable() {
            Toast.makeText(this@LoginActivity,"您的设备不支持指纹验证", Toast.LENGTH_SHORT).show()
        }
    }
}
