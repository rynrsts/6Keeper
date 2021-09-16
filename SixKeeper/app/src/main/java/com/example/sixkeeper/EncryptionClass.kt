package com.example.sixkeeper

import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class EncryptionClass {
    fun encrypt(data: String): ByteArray {                                                          // One-way hash
        val messageDigest = MessageDigest.getInstance("SHA-256")
        return messageDigest.digest(data.toByteArray(StandardCharsets.UTF_8))
    }
}