package com.example.sixkeeper

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class EncryptionClass {
    fun hashData(data: String): ByteArray {                                                         // One-way hash
        val messageDigest = MessageDigest.getInstance("SHA-256")
        return messageDigest.digest(data.toByteArray(StandardCharsets.UTF_8))
    }

//    fun encryptData(data: String): ByteArray {
//        val keyGenerator = KeyGenerator.getInstance("AES")
//        keyGenerator.init(256)
//        val secretKey: SecretKey = keyGenerator.generateKey()
//        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
////        val iv: ByteArray = cipher.iv
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
//        return cipher.doFinal(data.toByteArray())
//    }

//    fun decryptData(encryptedData: ByteArray): String {
//        val keyGenerator = KeyGenerator.getInstance("AES")
//        keyGenerator.init(256)
//        val secretKey: SecretKey = keyGenerator.generateKey()
//        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
////        val iv: ByteArray = cipher.iv
//        cipher.init(Cipher.DECRYPT_MODE, secretKey)
//        return cipher.doFinal(encryptedData).toString()
//    }
}