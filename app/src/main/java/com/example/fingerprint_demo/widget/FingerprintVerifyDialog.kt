package com.example.fingerprint_demo.widget

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.example.fingerprint_demo.R

/**
 * @description 指纹验证弹窗
 */
class FingerprintVerifyDialog(context: Context) : Dialog(context, R.style.TransparentDialogStyle) {

    private lateinit var onClickListener : OnClickListener
    private var tipText : TextView
    private var cancelBtn : TextView
    private var passBtn : TextView

    init {
        setContentView(R.layout.finger_verify_dialog)
        setCanceledOnTouchOutside(false)
        tipText = findViewById(R.id.finger_verify_tip)
        cancelBtn = findViewById(R.id.cancel_finger_verify_btn)
        passBtn = findViewById(R.id.pass_verify_btn)
        cancelBtn.setOnClickListener {
            onClickListener.onCancel()
            this.dismiss()
        }
        passBtn.setOnClickListener {
            onClickListener.usePass()
            this.dismiss()
        }
        windowAnim()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onClickListener.onDismiss()
    }

    fun setOnClickListener(onClickListener: OnClickListener) : FingerprintVerifyDialog{
        this.onClickListener = onClickListener
        return this
    }

    fun setTipText(tip : String,color : Int){
        tipText.text = tip
        tipText.setTextColor(color)
    }

    fun openPassLogin(){
        passBtn.visibility = View.VISIBLE
    }


    interface OnClickListener{
        /**
         * @description 取消验证
         */
        fun onCancel()
        /**
         * @description 关闭弹窗
         */
        fun onDismiss()

        /**
         * @description 使用密码
         */
        fun usePass()
    }

    private fun windowAnim() {
        val window = window!! //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim) //设置窗口弹出动画
        window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}