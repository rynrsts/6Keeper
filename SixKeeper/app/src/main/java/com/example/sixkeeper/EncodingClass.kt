package com.example.sixkeeper

import android.util.Base64

class EncodingClass {
    fun encodeData(data: String): String {                                                          // Encode data using Base64
        val encrypt = Base64.encode(data.toByteArray(), Base64.DEFAULT)
        return String(encrypt)
    }

    fun decodeData(data: String): String {                                                          // Decode data using Base64
        val decrypt = Base64.decode(data.toByteArray(), Base64.DEFAULT)
        return String(decrypt)
    }
}