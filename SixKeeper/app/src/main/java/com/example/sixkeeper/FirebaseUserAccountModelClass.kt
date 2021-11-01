package com.example.sixkeeper

class FirebaseUserAccountModelClass {
    private var userId: String? = null
    private var username: String? = null
    private var password: String? = null
    private var masterPin: String? = null
    private var creationDate: String? = null
    private var lastLogin: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var birthDate: String? = null
    private var email: String? = null
    private var mobileNumber: String? = null

    fun getUserId(): String? {
        return userId
    }

    fun setUserId(s: String?) {
        userId = s
    }

    fun getUsername(): String? {
        return username
    }

    fun setUsername(s: String?) {
        username = s
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(s: String?) {
        password = s
    }

    fun getMasterPin(): String? {
        return masterPin
    }

    fun setMasterPin(s: String?) {
        masterPin = s
    }

    fun getCreationDate(): String? {
        return creationDate
    }

    fun setCreationDate(s: String?) {
        creationDate = s
    }

    fun getLastLogin(): String? {
        return lastLogin
    }

    fun setLastLogin(s: String?) {
        lastLogin = s
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setFirstName(s: String?) {
        firstName = s
    }

    fun getLastName(): String? {
        return lastName
    }

    fun setLastName(s: String?) {
        lastName = s
    }

    fun getBirthDate(): String? {
        return birthDate
    }

    fun setBirthDate(s: String?) {
        birthDate = s
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(s: String?) {
        email = s
    }

    fun getMobileNumber(): String? {
        return mobileNumber
    }

    fun setMobileNumber(s: String?) {
        mobileNumber = s
    }
}