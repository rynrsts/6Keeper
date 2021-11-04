package com.example.sixkeeper

class FirebaseUserAccountModelClass {
    private var username: String? = null
    private var password: String? = null
    private var masterPin: String? = null
    private var status: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var birthDate: String? = null
    private var email: String? = null
    private var mobileNumber: String? = null
    private var profilePhoto: String? = null
    private var pwWrongAttempt: String? = null
    private var pwLockTime: String? = null
    private var mPinWrongAttempt: String? = null
    private var fWrongAttempt: String? = null
    private var mPinLockTime: String? = null

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

    fun getStatus(): String? {
        return status
    }

    fun setStatus(s: String?) {
        status = s
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

    fun getProfilePhoto(): String? {
        return profilePhoto
    }

    fun setProfilePhoto(s: String?) {
        profilePhoto = s
    }

    fun getPwWrongAttempt(): String? {
        return pwWrongAttempt
    }

    fun setPwWrongAttempt(s: String?) {
        pwWrongAttempt = s
    }

    fun getPwLockTime(): String? {
        return pwLockTime
    }

    fun setPwLockTime(s: String?) {
        pwLockTime = s
    }

    fun getMPinWrongAttempt(): String? {
        return mPinWrongAttempt
    }

    fun setMPinWrongAttempt(s: String?) {
        mPinWrongAttempt = s
    }

    fun getFWrongAttempt(): String? {
        return fWrongAttempt
    }

    fun setFWrongAttempt(s: String?) {
        fWrongAttempt = s
    }

    fun getMPinLockTime(): String? {
        return mPinLockTime
    }

    fun setMPinLockTime(s: String?) {
        mPinLockTime = s
    }
}