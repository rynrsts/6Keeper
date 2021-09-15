package com.example.sixkeeper

import android.annotation.SuppressLint
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionClass {
//    fun encrypt() {
//        val plaintext: ByteArray = ...
//        val keygen = KeyGenerator.getInstance("AES")
//        keygen.init(256)
//        val key: SecretKey = keygen.generateKey()
//        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
//        cipher.init(Cipher.ENCRYPT_MODE, key)
//        val ciphertext: ByteArray = cipher.doFinal(plaintext)
//        val iv: ByteArray = cipher.iv
//    }

    @SuppressLint("GetInstance")
    @Throws(Exception::class)
    fun encrypt(plaintext: ByteArray?, key: SecretKey, IV: ByteArray?): ByteArray? {
        val cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(key.encoded, "AES")
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        return cipher.doFinal(plaintext)
    }

    @SuppressLint("GetInstance")
    fun decrypt(cipherText: ByteArray?, key: SecretKey, IV: ByteArray?): String? {
        try {
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(key.encoded, "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decryptedText = cipher.doFinal(cipherText)

            return String(decryptedText)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return null
    }
}