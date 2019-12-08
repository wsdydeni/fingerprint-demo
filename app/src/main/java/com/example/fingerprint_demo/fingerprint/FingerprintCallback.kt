package com.example.fingerprint_demo.fingerprint

/**
 * @description 验证接口回调接口
 */
interface FingerprintCallback {

    /**
     * @description 无指纹硬件或硬件不可用
     */
    fun onHwUnavailable()

    /**
     * @description 未添加指纹
     */
    fun onNoneEnrolled()

    /**
     * @description 验证成功
     */
    fun onSucceeded()

    /**
     * @description 验证失败
     */
    fun onFailed()

    /**
     * @description 取消验证
     */
    fun onCancel()

}