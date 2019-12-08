package com.example.fingerprint_demo.fingerprint

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

class CipherHelper {

    private var _keystore : KeyStore

    init {
        _keystore = KeyStore.getInstance(KEYSTORE_NAME)
        _keystore.load(null)
    }

    companion object{
        const val KEY_NAME = "MyCipherHelper"
        const val KEYSTORE_NAME = "AndroidKeyStore"
        const val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        const val TRANSFORMATION = "$KEY_ALGORITHM/$BLOCK_MODE/$ENCRYPTION_PADDING"
    }

    fun createCipher(retry : Boolean) : Cipher {
        val key = GetKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        try {
            cipher.init(Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE,key)
        }catch (e : KeyPermanentlyInvalidatedException){
            _keystore.deleteEntry(KEY_NAME)
            if(retry){
                createCipher(false)
            }else{
                Exception("Could not create the cipher for fingerprint authentication.",e)
            }
        }
        return cipher
    }

    private fun GetKey() : Key {
        if(!_keystore.isKeyEntry(KEY_NAME)){
            CreateKey()
        }
        return _keystore.getKey(KEY_NAME,null)
    }

    /**
     * @description 生成密匙
     */
    private fun CreateKey(){
        val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM, KEYSTORE_NAME)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(KEY_NAME,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(BLOCK_MODE)
            .setEncryptionPaddings(ENCRYPTION_PADDING)
            .setUserAuthenticationRequired(true)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }
}