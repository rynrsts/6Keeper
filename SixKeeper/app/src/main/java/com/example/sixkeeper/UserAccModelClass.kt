package com.example.sixkeeper

class UserAccModelClass(
        val userId: String,
        val username: String,
        val password: ByteArray,
        val masterPin: ByteArray,
        val accountStatus: String,
        val creationDate: String,
        val lastLogin: String
)