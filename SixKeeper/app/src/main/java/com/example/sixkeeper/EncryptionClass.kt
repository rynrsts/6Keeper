package com.example.sixkeeper

import android.util.Base64
import java.util.*
import kotlin.collections.ArrayList

class EncryptionClass {
    fun encode(data: String): String {
        val encrypt = Base64.encode(data.toByteArray(), Base64.DEFAULT)                             // Encode (Base64)
        return String(encrypt)
    }

    fun decode(data: String): String {
        val decrypt = Base64.decode(data.toByteArray(), Base64.DEFAULT)                            // Decode (Base64)
        return String(decrypt)
    }

//    fun encrypt(data: String, key: ByteArray): String {
//        val keyGenerator = KeyGenerator.getInstance("AES")
//        keyGenerator.init(256)
//
//        val secretKey: SecretKey = SecretKeySpec(key, 0, key.size, "AES")
//        val ivParamSpec = IvParameterSpec(key)
//
//        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParamSpec)
//
//        val encryptedData = cipher.doFinal(data.toByteArray())                                      // Encrypt (AES)
//
//        return Base64.encodeToString(encryptedData, Base64.DEFAULT)                                 // Encode to String
//    }

    fun encodeS(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.DEFAULT)                                          // Encode to String
    }

//    fun decrypt(encryptedData: String, key: ByteArray): String {
//        val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)                              // Decode
//
//        val keyGenerator = KeyGenerator.getInstance("AES")
//        keyGenerator.init(256)
//
//        val secretKey: SecretKey = SecretKeySpec(key, 0, key.size, "AES")
//        val ivParamSpec = IvParameterSpec(key)
//
//        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
//        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParamSpec)
//
//        var decryptedData = ""
//
//        try {
//            decryptedData = String(cipher.doFinal(decodedData))                                     // Decrypt (AES)
//        } catch (e: Exception) {}
//
//        return decryptedData
//    }

    fun decodeBA(data: String): ByteArray {
        return Base64.decode(data, Base64.DEFAULT)                                                  // Decode
    }

//    fun hash(data: String): String {
//        val messageDigest = MessageDigest.getInstance("SHA-256")
//        val hashedData = messageDigest.digest(data.toByteArray(StandardCharsets.UTF_8))             // One-way hash (SHA)
//
//        return Base64.encodeToString(hashedData, Base64.DEFAULT)                                    // Encode to String
//    }

    fun encrypt(plaintext: String, userId: String): String {
        return sixKeeperEncryption(plaintext, userId, "yes")
    }

    fun decrypt(plaintext: String, userId: String): String {
        return sixKeeperDecryption(plaintext, userId)
    }

    fun encryptOnly(plaintext: String, userId: String): String {
        return sixKeeperEncryption(plaintext, userId, "no")
    }

    private fun sixKeeperEncryption(                                                                // 6Keeper Encryption Method
            plaintext: String, userId: String, allowDecrypt: String
    ): String {
        var singleDigit: Int                                                                        // Phase 1 Part 1
        var userIdTemp = userId

        do {
            singleDigit = 0

            for (i in userIdTemp.indices) {
                singleDigit += Integer.parseInt(userIdTemp[i].toString())
            }

            userIdTemp = singleDigit.toString()
        } while (singleDigit > 9)

        var phase1Data = ""                                                                         // Phase 1 Part 2

        for (i in plaintext.indices) {
            phase1Data = phase1Data + (plaintext[i] - singleDigit) + (plaintext[i] + singleDigit)
        }

        var phase2Data = ""                                                                         // Phase 2
        userIdTemp = userId
        var userIdIndex = 0

        for (i in phase1Data.indices) {
            phase2Data += phase1Data[i]

            if ((i + 1) % 2 == 0 && i != phase1Data.length - 1) {
                phase2Data += userIdTemp[userIdIndex]

                if (userIdIndex < userId.length - 1) {
                    userIdIndex++
                } else {
                    userIdIndex = 0
                }
            }
        }

        if (allowDecrypt == "no") {
            val phase2DataChar = phase2Data.toCharArray()
            Arrays.sort(phase2DataChar)
            phase2Data += String(phase2DataChar)
        }

        var phase3Data = ""                                                                         // Phase 3
        var columnIndex = 0
        val one = ArrayList<Char>(0)
        val two = ArrayList<Char>(0)
        val three = ArrayList<Char>(0)
        val four = ArrayList<Char>(0)
        val five = ArrayList<Char>(0)
        val six = ArrayList<Char>(0)
        val seven = ArrayList<Char>(0)
        val eight = ArrayList<Char>(0)
        val nine = ArrayList<Char>(0)
        val zero = ArrayList<Char>(0)
        var arrangement = ""
        var columnNum = "1234567890"

        for (char in phase2Data.toCharArray()) {
            when (columnIndex) {
                0 -> {
                    one.add(char)
                }
                1 -> {
                    two.add(char)
                }
                2 -> {
                    three.add(char)
                }
                3 -> {
                    four.add(char)
                }
                4 -> {
                    five.add(char)
                }
                5 -> {
                    six.add(char)
                }
                6 -> {
                    seven.add(char)
                }
                7 -> {
                    eight.add(char)
                }
                8 -> {
                    nine.add(char)
                }
            }

            if (columnIndex in 0..8) {
                columnIndex++
            } else if (columnIndex == 9) {
                zero.add(char)
                columnIndex = 0
            }
        }

        for (i in 1..2) {
            if (i == 1) {
                arrangement = userIdTemp
            } else if (i == 2) {
                arrangement = columnNum
            }

            for (char in arrangement.toCharArray()) {
                if (columnNum.contains(char)) {
                    when {
                        char.toString() == "1" -> {
                            for (columnChar in one.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "2" -> {
                            for (columnChar in two.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "3" -> {
                            for (columnChar in three.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "4" -> {
                            for (columnChar in four.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "5" -> {
                            for (columnChar in five.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "6" -> {
                            for (columnChar in six.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "7" -> {
                            for (columnChar in seven.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "8" -> {
                            for (columnChar in eight.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "9" -> {
                            for (columnChar in nine.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                        char.toString() == "0" -> {
                            for (columnChar in zero.toCharArray()) {
                                phase3Data += columnChar
                            }
                        }
                    }
                }

                if (i == 1) {
                    columnNum = columnNum.replace(char.toString(), "")
                }
            }
        }

        val phase4Part1Data = ArrayList<String>(0)                                                  // Phase 4 Part 1

        for (char in phase3Data.toCharArray()) {
            val binary = String.format(
                    "%8s",
                    Integer.toBinaryString(char.toInt())
            ).replace(
                    " ".toRegex(),
                    "0"
            )

            if (binary.length % 8 == 0) {
                phase4Part1Data.add(binary)
            } else {
                var paddedBinary = ""

                for (i in 1..(8 - (binary.length % 8))) {
                    paddedBinary += "0"
                }

                paddedBinary += binary
                phase4Part1Data.add(paddedBinary)
            }
        }

        val phase4Part2Data = ArrayList<String>(0)                                                  // Phase 4 Part 2

        for (i in phase4Part1Data.indices) {
            val oldBinary = phase4Part1Data[i]
            val length = oldBinary.length / 4
            val centerBinary = phase4Part1Data[i].substring(length, length * 3)
            val remainingBinary =
                    phase4Part1Data[i].substring(0, length) +
                            phase4Part1Data[i].substring(length * 3, oldBinary.length)
            val newBinary = remainingBinary + centerBinary

            phase4Part2Data.add(newBinary)
        }

        val phase5Data = StringBuilder()                                                            // Phase 5

        for (binary in phase4Part2Data) {
            phase5Data.append(binary.toInt(2).toChar())
        }

        return Base64.encodeToString(                                                               // Phase 6
                phase5Data.toString().toByteArray(),
                Base64.DEFAULT
        )
    }

    private fun sixKeeperDecryption(encryptedText: String, userId: String): String {                // 6Keeper Encryption Method (Decrypt)
        val phase6ReverseData = String(Base64.decode(encryptedText.toByteArray(), Base64.DEFAULT))  // Phase 6 Reverse

        val phase5ReverseData = ArrayList<String>(0)                                                // Phase 5 Reverse

        for (char in phase6ReverseData.toCharArray()) {
            val binary = String.format(
                    "%8s",
                    Integer.toBinaryString(char.toInt())
            ).replace(
                    " ".toRegex(),
                    "0"
            )

            if (binary.length % 8 == 0) {
                phase5ReverseData.add(binary)
            } else {
                var paddedBinary = ""

                for (i in 1..(8 - (binary.length % 8))) {
                    paddedBinary += "0"
                }

                paddedBinary += binary
                phase5ReverseData.add(paddedBinary)
            }
        }

        val phase4Part2ReverseData = ArrayList<String>(0)                                           // Phase 4 Part 2 Reverse

        for (i in phase5ReverseData.indices) {
            val oldBinary = phase5ReverseData[i]
            val length = oldBinary.length / 4
            val startingBinary = phase5ReverseData[i].substring(0, length)
            val centerBinary = phase5ReverseData[i].substring(length, length * 2)
            val endingBinary = phase5ReverseData[i].substring(length * 2, oldBinary.length)
            val newBinary = startingBinary + endingBinary + centerBinary

            phase4Part2ReverseData.add(newBinary)
        }

        val phase4Part1ReverseData = StringBuilder()                                                // Phase 4 Part 1 Reverse

        for (binary in phase4Part2ReverseData) {
            phase4Part1ReverseData.append(binary.toInt(2).toChar())
        }

        var phase3ReverseData = ""                                                              // Phase 3 Reverse
        val userIdChar = userId.toCharArray()
        var userIdTemp = ""
        var columnNum = "1234567890"
        val quotient = phase4Part1ReverseData.length / 10
        val remainder = phase4Part1ReverseData.length % 10
        val one: Queue<Char> = LinkedList()
        val two: Queue<Char> = LinkedList()
        val three: Queue<Char> = LinkedList()
        val four: Queue<Char> = LinkedList()
        val five: Queue<Char> = LinkedList()
        val six: Queue<Char> = LinkedList()
        val seven: Queue<Char> = LinkedList()
        val eight: Queue<Char> = LinkedList()
        val nine: Queue<Char> = LinkedList()
        val zero: Queue<Char> = LinkedList()
        var phase4Part1ReverseDataTemp = phase4Part1ReverseData.toString()
        var columnIndex = 0

        for (i in userIdChar.indices) {
            if (userIdTemp.indexOf(userIdChar[i]) == -1) {
                userIdTemp += userIdChar[i]
            }

            columnNum = columnNum.replace(userIdChar[i].toString(), "")
        }

        val arrangement: String = userIdTemp + columnNum

        for (char in arrangement.toCharArray()) {
            val column = Integer.parseInt(char.toString())
            var loop = quotient

            if (column <= remainder && column != 0) {
                loop++
            }

            for (i in 1..loop) {
                when (char.toString()) {
                    "1" -> {
                        one.add(phase4Part1ReverseDataTemp[0])
                    }
                    "2" -> {
                        two.add(phase4Part1ReverseDataTemp[0])
                    }
                    "3" -> {
                        three.add(phase4Part1ReverseDataTemp[0])
                    }
                    "4" -> {
                        four.add(phase4Part1ReverseDataTemp[0])
                    }
                    "5" -> {
                        five.add(phase4Part1ReverseDataTemp[0])
                    }
                    "6" -> {
                        six.add(phase4Part1ReverseDataTemp[0])
                    }
                    "7" -> {
                        seven.add(phase4Part1ReverseDataTemp[0])
                    }
                    "8" -> {
                        eight.add(phase4Part1ReverseDataTemp[0])
                    }
                    "9" -> {
                        nine.add(phase4Part1ReverseDataTemp[0])
                    }
                    "0" -> {
                        zero.add(phase4Part1ReverseDataTemp[0])
                    }
                }

                phase4Part1ReverseDataTemp = phase4Part1ReverseDataTemp.substring(
                        1,
                        phase4Part1ReverseDataTemp.length
                )
            }
        }

        for (i in phase4Part1ReverseData.indices) {
            when (columnIndex) {
                0 -> {
                    phase3ReverseData += one.poll()
                }
                1 -> {
                    phase3ReverseData += two.poll()
                }
                2 -> {
                    phase3ReverseData += three.poll()
                }
                3 -> {
                    phase3ReverseData += four.poll()
                }
                4 -> {
                    phase3ReverseData += five.poll()
                }
                5 -> {
                    phase3ReverseData += six.poll()
                }
                6 -> {
                    phase3ReverseData += seven.poll()
                }
                7 -> {
                    phase3ReverseData += eight.poll()
                }
                8 -> {
                    phase3ReverseData += nine.poll()
                }
            }

            if (columnIndex in 0..8) {
                columnIndex++
            } else if (columnIndex == 9) {
                phase3ReverseData += zero.poll()
                columnIndex = 0
            }
        }

        var phase2ReverseData = ""                                                                  // Phase 2 Reverse

        for (i in phase3ReverseData.indices) {
            if ((i + 1) % 3 != 0) {
                phase2ReverseData += phase3ReverseData[i]
            }
        }

        var phase1ReverseData = ""                                                                  // Phase 1 Reverse
        var singleDigit: Int
        userIdTemp = userId

        do {
            singleDigit = 0

            for (i in userIdTemp.indices) {
                singleDigit += Integer.parseInt(userIdTemp[i].toString())
            }

            userIdTemp = singleDigit.toString()
        } while (singleDigit > 9)

        for (i in phase2ReverseData.indices step 2) {
            val downToUp = phase2ReverseData[i] + singleDigit
            val topToDown = phase2ReverseData[i + 1] - singleDigit

            if (downToUp == topToDown) {
                phase1ReverseData += downToUp
            }
        }

        return phase1ReverseData
    }
}