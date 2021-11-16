package com.example.sixkeeper

import android.util.Base64
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionClass {
    fun encode(data: String): String {
        val encrypt = Base64.encode(data.toByteArray(), Base64.DEFAULT)                             // Encode (Base64)
        return String(encrypt)
    }

    fun decode(data: String): String {
        val decrypt = Base64.decode(data.toByteArray(), Base64.DEFAULT)                            // Decode (Base64)
        return String(decrypt)
    }

    fun encrypt(data: String, key: ByteArray): String {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)

        val secretKey: SecretKey = SecretKeySpec(key, 0, key.size, "AES")
        val ivParamSpec = IvParameterSpec(key)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParamSpec)

        val encryptedData = cipher.doFinal(data.toByteArray())                                      // Encrypt (AES)

        return Base64.encodeToString(encryptedData, Base64.DEFAULT)                                 // Encode to String
    }

    fun encodeS(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.DEFAULT)                                          // Encode to String
    }

    fun decrypt(encryptedData: String, key: ByteArray): String {
        val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)                              // Decode

        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)

        val secretKey: SecretKey = SecretKeySpec(key, 0, key.size, "AES")
        val ivParamSpec = IvParameterSpec(key)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParamSpec)

        var decryptedData = ""

        try {
            decryptedData = String(cipher.doFinal(decodedData))                                     // Decrypt (AES)
        } catch (e: Exception) {}

        return decryptedData
    }

    fun decodeBA(data: String): ByteArray {
        return Base64.decode(data, Base64.DEFAULT)                                                  // Decode
    }

    fun hash(data: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashedData = messageDigest.digest(data.toByteArray(StandardCharsets.UTF_8))             // One-way hash (SHA)

        return Base64.encodeToString(hashedData, Base64.DEFAULT)                                    // Encode to String
    }
}